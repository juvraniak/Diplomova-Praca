package liteshell.receivers;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;


/**
 * @author xvraniak@stuba.sk
 */

public class StringsPrepReceiver implements Receiver {

  final String[] metaCharacters = {"\""};

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings, Optional<Scope> scope) {
    try {
      String[] variables = strings[1].split("[+]");
      String reduce = Arrays.asList(variables).stream().map(v -> {
        boolean isCommand = v.startsWith("$(");
        boolean isInitializedVariable = v.startsWith("${");
        return escapeString(findValue(v, isCommand, isInitializedVariable, scope.get()));
      }).collect(Collectors.toList()).stream().reduce(String::concat).get();
      commandIO.setCommandOutput(CommandIO.prepareIO(reduce));
      commandIO.setReturnCode(0);
    } catch (Exception e) {
      commandIO.setCommandOutput(CommandIO.prepareIO(e.getMessage()));
      commandIO.setReturnCode(-1);
    }
    return commandIO;
  }

  private String escapeString(String str) {
    if (str.startsWith("\"") && str.endsWith("\"")) {
      str = str.substring(1, str.length() - 1);
    }
    for (int i = 0; i < metaCharacters.length; i++) {
      String meta = metaCharacters[i];
      if (str.contains(meta)) {
        str = str.replace(meta, "\\" + meta);
      }
    }
    return str;
  }
}
