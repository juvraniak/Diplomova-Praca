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

  private static final String BASE_URL = "http://localhost:8080/libraries/download/";
  private static String LIBS_PATH;
  private static final PluginFactory INSTANCE = new PluginFactory();
  @Getter
  @Setter
  private Map<String, ShellPlugin> shellPlugins;
  @Getter
  @Setter
  private Map<String, String> aliases;

  private PluginFactory() {
    LIBS_PATH = System.getProperty("user.dir").concat(File.separator).concat("libs");
    shellPlugins = new HashMap<>();
    registerAppCommands();
    registerCommands();
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

    try {
      URL website = new URL(BASE_URL);
      ReadableByteChannel rbc = Channels.newChannel(website.openStream());
      FileOutputStream fos = new FileOutputStream("libs/");
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    registerCommands();
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

  public void registerCommands() {
    List<Path> jars = new ArrayList<>();
    try {
      jars = Files.list(Paths.get(LIBS_PATH))
          .filter(Files::isRegularFile).collect(Collectors.toList());

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
    shellPlugins.put("grep", new GrepPlugin());
  }
}
