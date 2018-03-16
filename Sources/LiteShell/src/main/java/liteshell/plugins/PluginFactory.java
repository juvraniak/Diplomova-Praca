package liteshell.plugins;

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

import liteshell.commands.Command;
import liteshell.loaders.JarLoader;
import liteshell.utils.OperatingSystem;
import liteshell.utils.OsUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xvraniak@stuba.sk
 */

@Slf4j
public class PluginFactory {

    private static final String MAC_PATH = "/Users/jvraniak@sk.ibm.com/Documents/school/Diplomova-Praca/Sources/Commands/Libs/";
    private static final String LINUX_PATH = "/Users/jvraniak@sk.ibm.com/Documents/school/Diplomova-Praca/Sources/Commands/Libs/";
    private static final PluginFactory INSTANCE = new PluginFactory();
    @Getter
    @Setter
    private Map<String, ShellPlugin> shellPlugins;

    private PluginFactory() {
        shellPlugins = new HashMap<>();
        registerAppCommands();
        registerCommands();
    }

    public static PluginFactory init() {
        return INSTANCE;
    }

    public Command getCommand(String name) {
        return shellPlugins.get(name).getCommand();
    }

    public void addPlugin(final String pluginName) {
        //check whether command name is loaded - just in case
        if (shellPlugins.containsKey(pluginName)) {
            log.info("Command {} already loaded.", pluginName);
        }
        //check whether this command is in fs
        Optional<ShellPlugin> plugin = JarLoader.loadCommand(Paths.get(MAC_PATH, pluginName));
        if (plugin.isPresent()) {
            //remove different version if is loaded
            unloadPlugin(pluginName);
            //if yes load it
            addPlugin(plugin.get().getInfo().getName(), plugin.get());
        }
        //if not download and load

    }

    public void addPlugin(final String pluginName, final ShellPlugin shellPlugin) {
        shellPlugins.put(pluginName, shellPlugin);
    }

    public void unloadPlugin(final String pluginName) {
        String[] pluginInfo = pluginName.split("-");
        Optional<String> str = shellPlugins.keySet().stream().filter(key -> key.contains(pluginInfo[0])).findFirst();
        if (str.isPresent()) {
            shellPlugins.remove(str.get());
        }
    }

    public String listLoadedPlugin() {
        StringBuilder sb = new StringBuilder();
        shellPlugins.keySet().stream().forEach(s -> sb.append(s).append("\n"));
        return sb.toString();
    }

    public void registerCommands() {
        List<Path> jars = new ArrayList<>();
        try {
            if (OsUtils.getOperatingSystem().isPresent() && OsUtils.getOperatingSystem().get() == OperatingSystem.MAC) {
                jars = Files.list(Paths.get(MAC_PATH))
                    .filter(Files::isRegularFile).collect(Collectors.toList());
            }
            if (OsUtils.getOperatingSystem().isPresent() && OsUtils.getOperatingSystem().get() == OperatingSystem.LINUX) {
                jars = Files.list(Paths.get(LINUX_PATH))
                    .filter(Files::isRegularFile).collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        jars.forEach(path -> {
            Optional<ShellPlugin> plugin = JarLoader.loadCommand(path);
            if (plugin.isPresent()) {
                shellPlugins.put(plugin.get().getInfo().getName(), plugin.get());
            }
        });
    }

    private void registerAppCommands() {
//        shellPlugins.put("double", new DoublePlugin());
//        shellPlugins.put("pkg", new DownloadPlugin());
    }
}
