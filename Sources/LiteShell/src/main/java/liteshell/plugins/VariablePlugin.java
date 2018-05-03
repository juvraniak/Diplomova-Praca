package liteshell.plugins;

import liteshell.commands.Command;
import liteshell.commands.VariableCommand;

/**
 * @author xvraniak@stuba.sk
 */

public class VariablePlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("var", "1.0");
  }

  @Override
  public Command getCommand() {
    return new VariableCommand();
  }
}
