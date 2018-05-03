package liteshell.plugins;


import liteshell.commands.Command;
import liteshell.commands.GrepCommand;

/**
 * @author xvraniak@stuba.sk
 */

public class GrepPlugin implements ShellPlugin {
    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "grep");
    }

    @Override
    public Command getCommand() {
        return new GrepCommand();
    }

}
