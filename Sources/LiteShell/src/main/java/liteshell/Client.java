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
public class Client {

  private static final Client client = new Client();
  private PluginFactory pluginFactory;
  private ParserFactory parserFactory;
  private Executor executor;

  private Client() {
    pluginFactory = PluginFactory.init();
    parserFactory = ParserFactory.init();
    parserFactory.addParsers(pluginFactory.getShellPlugins());
    executor = new ExecutorImpl();
  }

  public static Client getInstance() {
    return client;
  }
}
