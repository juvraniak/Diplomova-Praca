package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeVariables;

/**
 * @author xvraniak@stuba.sk
 */

public class IntReceiver implements Receiver {

  @Override
  public CommandOutput executeCommand(String[] strings, Optional<Scope> scope) {
    CommandOutput out = new DefaultOutput();
    boolean isScopePresent = scope.isPresent();
    Integer integerValue = 0;

    String variableName = strings[1];

    if (isScopePresent && scope.get().getScopeVariables().getInitializedVariables()
        .contains(variableName)) {
      out.setCommandErrorOutput(
          Optional
              .of(Stream.of("Variable " + variableName + " is already defined in this scope!")));
      out.setReturnCode(-1);
      return out;
    }
    ScopeVariables var = scope.get().getScopeVariables();
    var.getInitializedVariables().add(variableName);
    var.getIntegerMap().put(variableName, integerValue);
    out.setReturnCode(0);
    if (strings.length > 2) {
      //means there is = sign lets find it
      //int i = 3
      if (strings[2].equals("=")) {
        try {
          integerValue = Integer.parseInt(strings[3]);
          var.getIntegerMap().put(variableName, integerValue);
        } catch (NumberFormatException e) {
          out.setCommandErrorOutput(Optional.of(Stream.of("Wrong format : " + e.getMessage())));
          out.setReturnCode(-1);
        }
      }
    }
    return out;
  }
}
