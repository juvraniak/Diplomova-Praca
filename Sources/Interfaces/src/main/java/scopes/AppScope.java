package scopes;

import commandcommons.CommandInputImpl;
import commands.Command;
import commands.DoubleCommand;
import plugins.ShellPlugin;

import java.util.List;
import java.util.Map;

public class AppScope implements ApplicationScope {
    private List<ShellPlugin> loadedPlugins;
    private final ScopeData scopeData = new ScopeData();
    Command command = new DoubleCommand();
    AppScope appScope = new AppScope();

    @Override
    public void run() {
        command.fireUpCommand(new CommandInputImpl("copy path1 path2"), appScope);
    }

    @Override
    public ApplicationScope getScope() {
        return this;
    }

    @Override
    public void setScopeParameters(Map<String, String> var1) {

    }

    @Override
    public ScopeData getScopeData() {
        return scopeData;
    }
}
