package sk.stuba.plugins;

import sk.stuba.commands.DoubleCommand;
import terminal.common.command.Command;
import terminal.common.plugins.PluginMeta;
import terminal.common.plugins.ShellPluging;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class DoublePlugin implements ShellPluging {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "double", "useles");
    }

    @Override
    public Command load() {
        return new DoubleCommand();
    }

    @Override
    public void unload(String s, String s1) {

    }
}
