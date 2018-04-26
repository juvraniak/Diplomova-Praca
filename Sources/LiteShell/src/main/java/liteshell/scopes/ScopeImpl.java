package liteshell.scopes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Stream;
import javafx.util.Pair;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
import liteshell.executors.Executor;
import liteshell.parsers.ParserFactory;
import liteshell.plugins.PluginFactory;
import liteshell.plugins.ShellPlugin;
import liteshell.utils.ShellClient;


/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class ScopeImpl implements Scope {


  protected static String scopeName;
  protected static Stack callStack;
  protected static PluginFactory pluginFactory;
  protected static ParserFactory parserFactory;
  protected static Executor executor;
  private final ScopeVariables scopeVariables = new ScopeVariables();
  List<Pair<ShellPlugin, String>> stack;

  public ScopeImpl(String scopeName, ShellClient shellClient) {
    this.scopeName = scopeName;
    this.callStack = new Stack();
    this.pluginFactory = shellClient.getPluginFactory();
    this.parserFactory = shellClient.getParserFactory();
    this.executor = shellClient.getExecutor();
    this.stack = new ArrayList<>();
    this.scopeVariables.getStringMap().put("pwd", System.getProperty("user.home"));
    this.scopeVariables.getInitializedVariables().add("pwd");
  }

  @Override
  public ScopeVariables getScopeVariables() {
    return scopeVariables;
  }

  @Override
  public String getCurrentWorkingDirectory() {
    return this.scopeVariables.getStringMap().get("pwd");
  }

  @Override
  public Optional<ShellPlugin> findShellPlugin(String command) {
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

  @Override
  public Scope getScope() {
    return this;
  }

  @Override
  public void addCommand(Pair<ShellPlugin, String> pair) {
    this.stack.add(pair);
  }

  @Override
  public void executeScript() {
    this.stack.forEach(k -> {
      CommandOutput out = k.getKey().getCommand()
          .execute(DefaultInput.of(Stream.of(k.getValue())), Optional.of(this));

      System.out.println(out.getReturnCode());
      out.getCommandOutput().get().forEach(System.out::println);
    });
  }
}
