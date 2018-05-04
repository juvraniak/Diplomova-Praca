package liteshell.v1.plugins;

import liteshell.commands.Command;
import liteshell.plugins.PluginMeta;
import liteshell.plugins.ShellPlugin;
import liteshell.v1.commands.AddCommand;

/**
 * @author xvraniak@stuba.sk
 */

public class AddPlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("1.0", "add");
  }

  @Override
  public Command getCommand() {
    return new AddCommand();
  }
}
