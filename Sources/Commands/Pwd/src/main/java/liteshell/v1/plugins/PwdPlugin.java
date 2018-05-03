package liteshell.v1.plugins;

import liteshell.commands.Command;
import liteshell.plugins.PluginMeta;
import liteshell.plugins.ShellPlugin;
import liteshell.v1.commands.PwdCommand;


/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com) created : 14/01/2018.
 */

public class PwdPlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("1.0", "pwd");
  }

  @Override
  public Command getCommand() {
    return new PwdCommand();
  }

}
