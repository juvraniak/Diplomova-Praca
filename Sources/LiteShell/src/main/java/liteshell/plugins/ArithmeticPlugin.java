package liteshell.plugins;

import liteshell.commands.ArithmeticCommand;
import liteshell.commands.Command;

/**
 * @author xvraniak@stuba.sk
 */

public class ArithmeticPlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("1.0", "arithmetic");
  }

  @Override
  public Command getCommand() {
    return new ArithmeticCommand();
  }
}
