package plugins;

import java.util.HashMap;
import java.util.Map;

public class PluginFactory {
    private Map<String, ShellPlugin> listOfCommands;

    private PluginFactory(){
        listOfCommands = new HashMap<>();
    }

    public void addCommand(final String commandName, final ShellPlugin shellPlugin){
        listOfCommands.put(commandName, shellPlugin);
    }

    public String listLoadedPlugin()

    public PluginFactory init(){
        PluginFactory pluginFactory = new PluginFactory();
        pluginFactory.
    }
}
