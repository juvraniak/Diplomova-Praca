package liteshell.plugins;

import liteshell.commands.Command;
import liteshell.commands.PackageCommand;

/**
 * @author xvraniak@stuba.sk
 */

public class PackagePlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("pkg", "1.0");
  }

  @Override
  public Command getCommand() {
    return new PackageCommand();
  }
  
}
