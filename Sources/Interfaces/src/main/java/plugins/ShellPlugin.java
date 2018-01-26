package plugins;

import commands.Command;

public interface ShellPlugin {
    PluginMeta getInfo();

    Command load();

    void unload(String var1, String var2);
}
