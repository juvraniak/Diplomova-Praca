package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Collectors;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Receiver {

  CommandIO executeCommand(CommandIO commandIO, String[] args, Optional<Scope> scope);

  default String findValue(String string, boolean isCommand, Scope scope) {
    string =
        string.startsWith("$(") ? string.substring(string.indexOf("(") + 1, string.indexOf(")"))
            : string;
    return isCommand ? findAndExecuteCommand(
        string, scope) : string;
  }

  default String findAndExecuteCommand(String string, Scope scope) {
    CommandIO out = scope.getExecutor().execute(string, scope);
    String str = "";
    if (out.getReturnCode() == 0) {

      Optional<String> reduce = out.getCommandOutput().get().collect(Collectors.toList())
          .stream()
          .reduce(String::concat);
      if (reduce.isPresent()) {
        return reduce.get();
      }
    }
    return str;
  }
}
