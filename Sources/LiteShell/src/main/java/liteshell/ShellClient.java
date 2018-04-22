package liteshell;

import liteshell.executors.Executor;
import liteshell.executors.ExecutorImpl;
import liteshell.parsers.ParserFactory;
import liteshell.plugins.PluginFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xvraniak@stuba.sk
 */

@Getter
@Setter
public class ShellClient {

  private static final ShellClient SHELL_CLIENT = new ShellClient();
  private PluginFactory pluginFactory;
  private ParserFactory parserFactory;
  private Executor executor;

  private ShellClient() {
    pluginFactory = PluginFactory.init();
    parserFactory = ParserFactory.init();
    parserFactory.addParsers(pluginFactory.getShellPlugins());
    executor = new ExecutorImpl();
  }

  public static ShellClient getInstance() {
    return SHELL_CLIENT;
  }
}
