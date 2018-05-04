package liteshell.scopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.util.Pair;
import liteshell.exceptions.UnknownCommandException;
import liteshell.executors.Executor;
import liteshell.keywords.Keyword;
import liteshell.plugins.PluginFactory;
import liteshell.plugins.ShellPlugin;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xvraniak@stuba.sk
 */
@Slf4j
public class AbstractScope implements Scope, Runnable {

  protected static String scopeName;
  protected PluginFactory pluginFactory;
  @Getter
  protected Executor executor;
  protected final ScopeVariables scopeVariables = new ScopeVariables();
  protected Scope parent;

  @Getter
  @Setter
  protected List<Pair<String, String>> inputParameters = new ArrayList<>();
  protected List<String> stack = new ArrayList<>();
  @Getter
  @Setter
  protected String returnType;
  @Getter
  @Setter
  protected String returnValue;
  @Getter
  @Setter
  protected boolean override;
  @Getter
  @Setter
  protected Map<String, ScopeImpl> functions = new HashMap<>();

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
      } else {
        List<String> collect = pluginFactory.getShellPlugins().keySet().stream()
            .filter(key -> command.startsWith(key)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
          searchedPlugin = Optional.of(pluginFactory.getShellPlugins().get(collect.get(0)));
        }
      }
    }
    return searchedPlugin;
  }

  @Override
  public void addCommand(String s) {
    this.stack.add(s);
  }

  @Override
  public void executeScript() {
    //execute everything before main
    this.stack.forEach(k -> {
      executor.execute(k, getScope());
    });
    this.functions.get("main").stack.forEach(v ->
        executor.execute(v, functions.get("main").getScope()));
  }

  @Override
  public void run() {
    printLine();
    String userInput = "";
    while (true) {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      try {
        userInput = prepareInput(br.readLine());
        userInput = "$(add($(add(${i}, ${j})), 5));";
        executor.execute(userInput, getScope());
      } catch (UnknownCommandException ex) {
        log.error("There was issue with following command: \n {}", ex.getMessage());
      } catch (IOException ex) {
        log.error("Problem reading from command line:\n {}", ex.getMessage());
      }
      if (userInput.equals("$(exit);")) {
        System.exit(0);
      }
      printLine();
    }
  }

  private String prepareInput(String in) {
    return in.startsWith("./") ? in
        : in.endsWith(";") ? "$(" + in.substring(0, in.length() - 1) + ");" : "$(" + in + ");";
  }

  protected void loadSystemVariables() {
    scopeVariables.getStringMap().putAll(System.getenv());
    scopeVariables.getInitializedVariables().putAll(scopeVariables.getStringMap().keySet().stream()
        .collect(Collectors.toMap(a -> a, a -> Keyword.STRING)));
  }

  private void printLine() {
    System.out.print("> ");
  }
}
