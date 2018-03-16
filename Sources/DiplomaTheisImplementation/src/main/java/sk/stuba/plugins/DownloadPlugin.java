package sk.stuba.plugins;

import sk.stuba.commands.DownloadLibraryCommand;
import terminal.common.command.Command;
import terminal.common.plugins.PluginMeta;
import terminal.common.plugins.ShellPluging;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class DownloadPlugin implements ShellPluging {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "pkg", "useles");
    }

    @Override
    public Command load() {
        return new DownloadLibraryCommand();
    }

    @Override
    public void unload(String s, String s1) {

    }
}
