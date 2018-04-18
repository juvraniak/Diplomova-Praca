package liteshell.scopes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import liteshell.Client;
import liteshell.executors.Executor;
import liteshell.parsers.ParserFactory;
import liteshell.plugins.PluginFactory;
import liteshell.plugins.ShellPlugin;


/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class ScopeImpl implements Scope {

    protected static String scopeName;
    protected static Stack callStack;
    protected static PluginFactory pluginFactory;
  protected static ParserFactory parserFactory;
    protected static Executor executor;
    private final ScopeData scopeData = new ScopeData();
  List<Map<ShellPlugin, String>> stack;

  public ScopeImpl(String scopeName, Client client) {
    this.scopeName = scopeName;
    this.callStack = new Stack();
    this.pluginFactory = client.getPluginFactory();
    this.parserFactory = client.getParserFactory();
    this.executor = client.getExecutor();
    }

    @Override
    public ScopeData getScopeData() {
        return scopeData;
    }

  protected Optional<ShellPlugin> findShellPlugin(String command) {
    Optional<ShellPlugin> searchedPlugin = Optional.empty();
    if (command.startsWith("./")) {
      return searchedPlugin;
    } else {
      String searchedCommand = command.split(" ")[0];
      ShellPlugin plugin = pluginFactory.getShellPlugins().get(searchedCommand);
      if (plugin != null) {
        searchedPlugin = Optional.of(plugin);
      }
    }
    return searchedPlugin;
  }
}
