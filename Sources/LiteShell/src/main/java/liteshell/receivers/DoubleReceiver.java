package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.keywords.Keyword;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeVariables;

/**
 * @author xvraniak@stuba.sk
 */

public class DoubleReceiver implements Receiver {

  @Override
  public CommandOutput executeCommand(String[] strings, Optional<Scope> scope) {
    CommandOutput out = new DefaultOutput();
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
        doubleValue = Double.parseDouble(strings[2]);
        var.getDoubleMap().put(variableName, doubleValue);
      } catch (NumberFormatException e) {
        out.setCommandErrorOutput(Optional.of(Stream.of("Wrong format : " + e.getMessage())));
        out.setReturnCode(-1);
      }
    }
    return out;
  }
}
