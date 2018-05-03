package liteshell.parser;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
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
    CommandOutput out = plugin.getCommand()
        .execute(DefaultInput.of(Stream.of("pkg download move-command-1.0")), Optional.empty());

    PluginFactory factory = PluginFactory.get();
    
  }
}
