package liteshell.plugins;

import liteshell.commands.Command;

/**
 * @author xvraniak@stuba.sk
 */

public interface ShellPlugin {

    PluginMeta getInfo();

    Command getCommand();

    boolean shouldPrint();
}
