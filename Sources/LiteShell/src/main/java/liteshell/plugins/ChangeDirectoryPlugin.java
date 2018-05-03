package liteshell.plugins;

import liteshell.commands.ChangeDirectoryCommand;
import liteshell.commands.Command;

/**
 * @author xvraniak@stuba.sk
 */

public class ChangeDirectoryPlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("1.0", "cd");
  }

  @Override
  public Command getCommand() {
    return new ChangeDirectoryCommand();
  }
}
