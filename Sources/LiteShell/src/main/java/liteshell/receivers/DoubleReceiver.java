package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.keywords.Keyword;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeVariables;

/**
 * @author xvraniak@stuba.sk
 */

public class DoubleReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO out, String[] strings, Optional<Scope> scope) {

    boolean isScopePresent = scope.isPresent();
    Double doubleValue = 0D;

    String variableName = strings[1];

    if (isScopePresent && scope.get().getScopeVariables().getInitializedVariables()
        .get(variableName) != null) {
      out.setCommandErrorOutput(
          Optional
              .of(Stream.of("Variable " + variableName + " is already defined in this scope!")));
      out.setReturnCode(-1);
      return out;
    }
    ScopeVariables var = scope.get().getScopeVariables();
    var.getInitializedVariables().put(variableName, Keyword.DOUBLE);
    var.getDoubleMap().put(variableName, doubleValue);
    out.setReturnCode(0);
    if (strings.length > 2) {
      try {
        boolean isCommand = strings[2].startsWith("$(");
        boolean isInitializedVariable = strings[2].startsWith("${");
        String toExecute = isCommand ? "arithmetic" : strings[2];
        String replacement = findValue(toExecute, isCommand, isInitializedVariable, scope.get());

        doubleValue = Double.parseDouble(replacement);
        var.getDoubleMap().put(variableName, doubleValue);
      } catch (NumberFormatException e) {
        out.setCommandErrorOutput(Optional.of(Stream.of("Wrong format : " + e.getMessage())));
        out.setReturnCode(-1);
      }
    }
    return out;
  }
}
