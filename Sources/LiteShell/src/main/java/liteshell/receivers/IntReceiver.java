package liteshell.receivers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.keywords.Keyword;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeVariables;

/**
 * @author xvraniak@stuba.sk
 */

public class IntReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO out, String[] strings, Optional<Scope> scope) {
    boolean isScopePresent = scope.isPresent();
    Integer integerValue = 0;

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
    var.getInitializedVariables().put(variableName, Keyword.INT);
    var.getIntegerMap().put(variableName, integerValue);
    out.setReturnCode(0);
    if (strings.length > 2) {
        try {
          boolean isCommand = strings[2].startsWith("$(");
          boolean isInitializedVariable = strings[2].startsWith("${");
          String toExecute = isCommand ? "$(arithmetic " + strings[2]
              .substring(strings[2].indexOf("(") + 1, strings[2].length()) : strings[2];
          String replacement = findValue(toExecute, isCommand, isInitializedVariable, scope.get());
          BigDecimal bd = new BigDecimal(replacement);
          integerValue = bd.intValue();
          var.getIntegerMap().put(variableName, integerValue);
        } catch (NumberFormatException e) {
          //TODO: if parseInt fail we may need to check whether it is command there...
          out.setCommandErrorOutput(Optional.of(Stream.of("Wrong format : " + e.getMessage())));
          out.setReturnCode(-1);
        }
    }
    return out;
  }
}
