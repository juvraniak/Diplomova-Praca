package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.plugins.PluginFactory;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class DownloadPackageReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO out, String[] strings, Optional<Scope> optional) {
    if (strings.length != 3) {
      out.setReturnCode(-1);
      out.setCommandErrorOutput(Optional.of(Stream.of("Wrong number of arguments!")));
      return out;
    }
    PluginFactory factory = PluginFactory.get();
    try {
      factory.addPlugin(strings[2]);
      out.setCommandOutput(Optional.of(Stream.of("Command was downloaded.")));
      out.setReturnCode(0);
    } catch (Exception e) {
      out.setReturnCode(-1);
      out.setCommandErrorOutput(Optional.of(Stream.of("Name of command is wrong.")));
    }
    return out;
  }
}
