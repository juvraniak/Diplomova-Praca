package sk.stuba.plugins;

import sk.stuba.commands.IntCommand;
import terminal.common.command.Command;
import terminal.common.plugins.PluginMeta;
import terminal.common.plugins.ShellPluging;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class IntPlugin implements ShellPluging {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "int", "sk.stuba.plugins");
    }

    @Override
    public Command load() {
        return new IntCommand();
    }

    @Override
    public void unload(String s, String s1) {

    }
}
