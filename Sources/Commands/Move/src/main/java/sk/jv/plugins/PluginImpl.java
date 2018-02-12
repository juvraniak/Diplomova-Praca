package sk.jv.plugins;

import sk.jv.commands.MoveCommand;
import terminal.common.command.Command;
import terminal.common.plugins.PluginMeta;
import terminal.common.plugins.ShellPluging;

/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class PluginImpl implements ShellPluging{

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "move", "sk.stuba.plugins");
    }

    @Override
    public Command load() {
        return new MoveCommand();
    }

    @Override
    public void unload(String s, String s1) {

    }

}
