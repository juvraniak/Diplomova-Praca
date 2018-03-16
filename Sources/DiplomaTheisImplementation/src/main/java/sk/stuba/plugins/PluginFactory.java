package sk.stuba.plugins;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import terminal.common.command.Command;
import terminal.common.plugins.ShellPluging;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */
@Slf4j
public class PluginFactory {

    private static final String MAC_PATH = "/Users/jvraniak@sk.ibm.com/Documents/school/Diplomova-Praca/Sources/Commands/Libs/";
    private static final String LINUX_PATH = "/Users/jvraniak@sk.ibm.com/Documents/school/Diplomova-Praca/Sources/Commands/Libs/";
    private static PluginFactory instance = new PluginFactory();
    @Getter
    @Setter
    private static Map<String, ShellPluging> commands;

    private PluginFactory() {
        commands = new HashMap<>();
        registerAppCommands();
        registerCommands();
    }

    public static PluginFactory init() {
        return instance;
    }

    public static Command getCommand(String name) {
        return commands.get(name).load();
    }

    public void addPlugin(final String pluginName) {
        //check whether command name is loaded - just in case
        if (commands.containsKey(pluginName)) {
            log.info("Command {} already loaded.", pluginName);
        }
        //check whether this command is in fs
        Optional<ShellPluging> plugin = JarLoader.loadJar(Paths.get(MAC_PATH, pluginName));
        if (plugin.isPresent()) {
            //remove different version if is loaded
            unloadPlugin(pluginName);
            //if yes load it
            addPlugin(plugin.get().getInfo().getName(), plugin.get());
        }
        //if not download and load

    }

    public void addPlugin(final String pluginName, final ShellPluging shellPlugin) {
        commands.put(pluginName, shellPlugin);
    }

    public void unloadPlugin(final String pluginName) {
        String[] pluginInfo = pluginName.split("-");
        Optional<String> str = commands.keySet().stream().filter(key -> key.contains(pluginInfo[0])).findFirst();
        if (str.isPresent()) {
            commands.remove(str.get());
        }
    }

    public String listLoadedPlugin() {
        StringBuilder sb = new StringBuilder();
        commands.keySet().stream().forEach(s -> sb.append(s).append("\n"));
        return sb.toString();
    }

    public void registerCommands() {
        List<Path> jars = new ArrayList<>();
        try {
            jars = Files.list(Paths.get(MAC_PATH))
                .filter(Files::isRegularFile).collect(Collectors.toList());

//            jars = Files.list(Paths.get(LINUX_PATH))
//                .filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        jars.forEach(path -> {
            Optional<ShellPluging> plugin = JarLoader.loadJar(path);
            if (plugin.isPresent()) {
                commands.put(plugin.get().getInfo().getName(), plugin.get());
            }
        });
    }

    private void registerAppCommands() {
        commands.put("double", new DoublePlugin());
        commands.put("pkg", new DownloadPlugin());
    }
}