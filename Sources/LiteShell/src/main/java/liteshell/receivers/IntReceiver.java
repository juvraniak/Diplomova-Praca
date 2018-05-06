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
          integerValue = Integer.parseInt(strings[2]);
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
