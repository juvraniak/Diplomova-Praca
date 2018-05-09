package liteshell.plugins;

import liteshell.commands.Command;
import liteshell.commands.EchoCommand;

/**
 * @author xvraniak@stuba.sk
 */

public class EchoPlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("1.0", "echo");
  }

  @Override
  public Command getCommand() {
    return new EchoCommand();
  }
}
