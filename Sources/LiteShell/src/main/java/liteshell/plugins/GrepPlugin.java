package liteshell.plugins;


import liteshell.commands.Command;
import liteshell.commands.GrepCommand;

import java.util.Optional;

/**
 * @author xvraniak@stuba.sk
 */

public class GrepPlugin implements ShellPlugin {
    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "grep", Optional.empty());
    }

    @Override
    public Command getCommand() {
        return new GrepCommand();
    }

    @Override
    public boolean shouldPrint() {
        return true;
    }
}
