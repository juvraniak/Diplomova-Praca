package terminal.common.plugins;

import terminal.common.command.Command;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 * @version 1.0
 * created : 08/01/2018.
 */

public interface ShellPluging {
    PluginMeta getInfo();
    Command load();
    void unload(String name, String version);
}
