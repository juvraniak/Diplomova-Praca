package liteshell.plugins;

import liteshell.commands.Command;

/**
 * @author xvraniak@stuba.sk
 */

public class FunctionPlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("function", "1.0");
  }

  @Override
  public Command getCommand() {
    return null;
  }

}
