package sk.jv.plugins;

import sk.jv.commands.CopyCommand;
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
        return new PluginMeta("1.0", "copy", "sk.stuba.plugins");
    }

    @Override
    public Command load() {
        return new CopyCommand();
    }

    @Override
    public void unload(String s, String s1) {

    }

}
