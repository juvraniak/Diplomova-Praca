package liteshell.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import liteshell.commands.Command;
import liteshell.loaders.JarLoader;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author xvraniak@stuba.sk
 */


public class PluginFactory {

  private static final Logger log = LoggerFactory.getLogger(PluginFactory.class);
  private static final PluginFactory INSTANCE = new PluginFactory();
  private static final String BASE_URL = "http://localhost:8080/libraries/download/";
  private static String LIBS_PATH;

  @Getter
  @Setter
  private Map<String, ShellPlugin> shellPlugins;
  @Getter
  @Setter
  private Map<String, String> aliases;
  private Map<PluginMeta, ShellPlugin> allAvailablePlugins;

  private PluginFactory() {
    LIBS_PATH = System.getProperty("user.dir").concat(File.separator).concat("libs");
    shellPlugins = new HashMap<>();
    allAvailablePlugins = new HashMap<>();
    registerAppCommands();
    registerAllPlugins();
    loadPlugins();
  }

  private void loadPlugins() {
    allAvailablePlugins.forEach((k, v) -> {
      if (!shellPlugins.containsKey(k.getName())) {
        shellPlugins.put(k.getName(), v);
      } else {
        if (shellPlugins.get(k.getName()).getInfo().getVersion().compareTo(k.getVersion()) < 0) {
          shellPlugins.put(k.getName(), v);
        }
      }
    });
  }

  public static PluginFactory get() {
    return INSTANCE;
  }

  public Command getCommand(String name) {
    return shellPlugins.get(name).getCommand();
  }

  public void addPlugin(final String pluginName) throws IOException {
    //check whether command name is loaded - just in case
    if (shellPlugins.containsKey(pluginName)) {
      log.info("Command {} already loaded.", pluginName);
    }
    //check whether this command is in fs
    Optional<ShellPlugin> plugin = JarLoader.loadCommand(Paths.get(LIBS_PATH, pluginName));
    if (plugin.isPresent()) {
      //remove different version if is loaded
      unloadPlugin(pluginName);
      //if yes load it
      addPlugin(plugin.get().getInfo().getName(), plugin.get());
    }
    //if not download and load
    downloadAndLoad(pluginName);
  }

  private void downloadAndLoad(String pluginName) throws IOException {
    if (!pluginName.endsWith(".jar")) {
      pluginName = pluginName + ".jar";
    }

    URL website = new URL(BASE_URL + pluginName);
    try (ReadableByteChannel rbc = Channels
        .newChannel(website.openStream()); FileOutputStream fos = new FileOutputStream(
        "libs/" + pluginName + ".jar")) {
//      URL website = new URL(BASE_URL);
//      ReadableByteChannel rbc = Channels.newChannel(website.openStream());
//      FileOutputStream fos = new FileOutputStream("libs/");
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);


    } catch (Exception ex) {
      ex.printStackTrace();
    }
    //FIXME: bud restartovat konzolu, alebo prist na to preco sa to neulozi do vypnutia programu


  }

  public void addPlugin(final String pluginName, final ShellPlugin shellPlugin) {
    shellPlugins.put(pluginName, shellPlugin);
  }

  public void unloadPlugin(final String pluginName) {
    String[] pluginInfo = pluginName.split("-");
    Optional<String> str = shellPlugins.keySet().stream().filter(key -> key.contains(pluginInfo[0]))
        .findFirst();
    if (str.isPresent()) {
      shellPlugins.remove(str.get());
    }
  }

  public String listLoadedPlugin() {
    StringBuilder sb = new StringBuilder();
    shellPlugins.keySet().stream().forEach(s -> sb.append(s).append("\n"));
    return sb.toString();
  }

  private void registerAllPlugins() {
    List<Path> pathsToLibraries = new ArrayList<>();
    try {
      pathsToLibraries = Files.list(Paths.get(LIBS_PATH))
          .filter(Files::isRegularFile).collect(Collectors.toList());
    } catch (IOException e) {
      log.debug(e.getMessage());
    }
    pathsToLibraries.forEach(path -> {
      log.info("Loading {}", path);

      Optional<ShellPlugin> plugin = JarLoader.loadCommand(path);
      if (plugin.isPresent()) {
        ShellPlugin p = plugin.get();
        PluginMeta meta = p.getInfo();
        log.info("Loaded plugin : {}, version : {}", meta.getName(), meta.getVersion());
        allAvailablePlugins.put(meta, p);

      }
    });
  }

  private void registerAppCommands() {
    allAvailablePlugins.put(new PluginMeta("1.0", "grep"), new GrepPlugin());
    allAvailablePlugins.put(new PluginMeta("1.0", "pgk"), new PackagePlugin());
    allAvailablePlugins.put(new PluginMeta("1.0", "int"), new VariablePlugin());
    allAvailablePlugins.put(new PluginMeta("1.0", "double"), new VariablePlugin());
    allAvailablePlugins.put(new PluginMeta("1.0", "string"), new VariablePlugin());
    allAvailablePlugins.put(new PluginMeta("1.0", "arithmetic"), new VariablePlugin());
    allAvailablePlugins.put(new PluginMeta("1.0", "stringsPrep"), new VariablePlugin());
    allAvailablePlugins.put(new PluginMeta("1.0", "${"), new VariablePlugin());
    allAvailablePlugins.put(new PluginMeta("1.0", "cd"), new ChangeDirectoryPlugin());
//    allAvailablePlugins.put(new ArithmeticPlugin().getInfo(), new ArithmeticPlugin());
  }

  public boolean changePlugin(String usePlugin) {
    String[] parameters = usePlugin.split(" ");
    String searchedPlugin = parameters[0];
    String version = parameters[1];

    log.info(
        "Will try to use in pluginFactory specified plugin " + searchedPlugin + " version : "
            + version);

    if (shellPlugins.containsKey(searchedPlugin) && shellPlugins.get(searchedPlugin).getInfo()
        .getVersion().equals(version)) {
      return true;
    }

    Optional<Entry<PluginMeta, ShellPlugin>> replacement = allAvailablePlugins.entrySet()
        .stream().filter(
            p -> p.getKey().getName().equals(searchedPlugin) && p.getKey().getVersion()
                .equals(version))
        .findFirst();

    if (replacement.isPresent()) {
      shellPlugins.put(searchedPlugin, replacement.get().getValue());
      return true;
    }
    return false;
  }

}
