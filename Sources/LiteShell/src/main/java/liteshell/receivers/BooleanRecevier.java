package liteshell.receivers;

import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.exceptions.WrongCommandInputException;
import liteshell.keywords.Keyword;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeVariables;

/**
 * @author xvraniak@stuba.sk
 */

public class BooleanRecevier implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings, Optional<Scope> scope) {
    boolean isScopePresent = scope.isPresent();
    Boolean booleanValue = false;

    String variableName = strings[1];

//    if (isScopePresent && scope.get().getScopeVariables().getInitializedVariables()
//        .get(variableName) != null) {
//      commandIO.setCommandErrorOutput(
//          Optional
//              .of(Stream.of("Variable " + variableName + " is already defined in this scope!")));
//      commandIO.setReturnCode(-1);
//      return commandIO;
//    }
    ScopeVariables var = scope.get().getScopeVariables();
    var.getInitializedVariables().put(variableName, Keyword.BOOLEAN);
    var.getBooleanMap().put(variableName, booleanValue);
    commandIO.setReturnCode(0);
    if (strings.length > 2) {
      try {
        boolean isCommand = strings[2].startsWith("$(");
        boolean isInitializedVariable = strings[2].startsWith("${");
        String toExecute = isCommand ? "$(booleanPrep " + strings[2]
            .substring(strings[2].indexOf("(") + 1, strings[2].length()) : strings[2];
        String replacement = findValue(toExecute, isCommand, isInitializedVariable, scope.get());

        booleanValue = resolveBoolean(replacement);
        if (booleanValue != null) {
          var.getBooleanMap().put(variableName, booleanValue);
        } else {
          throw new WrongCommandInputException("Could not resolve this input: " + strings[2]);
        }
      } catch (WrongCommandInputException e) {
        commandIO.setCommandErrorOutput(CommandIO.prepareIO("Wrong format : " + e.getMessage()));
        commandIO.setReturnCode(-1);
      }
    }
    return commandIO;
  }

  private Boolean resolveBoolean(String replacement) {
    return replacement.equals("true") ? true : replacement.equals("false") ? false : null;
  }
}
