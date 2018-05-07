package liteshell.parser;

import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.commands.ios.DefaultCommadIO;
import liteshell.plugins.PackagePlugin;
import liteshell.plugins.PluginFactory;
import liteshell.plugins.ShellPlugin;
import org.junit.Test;

/**
 * @author xvraniak@stuba.sk
 */

public class ImportTest {

  @Test
  public void testDownloadCommand() {
    ShellPlugin plugin = new PackagePlugin();
    CommandIO out = plugin.getCommand()
        .execute(DefaultCommadIO.of(CommandIO.prepareIO("pkg download move-command-1.0")),
            Optional.empty());

    PluginFactory factory = PluginFactory.get();
    
  }
}
