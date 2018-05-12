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
import liteshell.commands.ios.CommandIO;
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
public class AbstractScope implements Scope, Runnable, Cloneable {

  @Getter
  protected String scopeName;
  protected PluginFactory pluginFactory;
  @Getter
  protected Executor executor;
  @Getter
  @Setter
  protected ScopeVariables scopeVariables = new ScopeVariables();
  @Getter
  protected ScopeImpl parent;

  @Getter
  @Setter
  protected List<Pair<String, String>> inputParameters = new ArrayList<>();
  protected List<String> stack = new ArrayList<>();
  @Getter
  @Setter
  protected Keyword returnType;
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
  public void executeScript(String function, ScopeVariables scopeVariables) {
    CommandIO out;
    if (scopeName.equals("script")) {
      this.stack.forEach(k -> {
        executor.execute(k, getScope());
      });
    }
    String fName = function.substring(0, function.indexOf("("));
    ScopeImpl parent = (ScopeImpl) this.getParent();

    ScopeImpl functionScope = parent.functions.get(fName);
    functionScope.setScopeVariables(scopeVariables);
    for (String command : functionScope.stack) {
      if (command.startsWith("$(")) {
        out = executor.execute(command, functionScope);
        if (out.getReturnCode() == 0) {
          out.getCommandOutput()
              .ifPresent(
                  stringStream -> System.out.println(stringStream.reduce(String::concat).get()));
        } else {
          out.getCommandErrorOutput().ifPresent(System.out::println);
        }
      } else if (command.startsWith("fcall ")) {
        String[] split = command.split(" = ");
        String callParams;
        String afterExecuteRetValue;
        if (split.length == 1) {
          fName = command.substring("fcall ".length());
        } else {
          fName = split[1];
          afterExecuteRetValue = split[0].substring("fcall ".length());
          callParams = split[1].substring(split[1].indexOf("(") + 1, split[1].length() - 1);
        }

        try {
          execute(fName, parent, functionScope);
        } catch (CloneNotSupportedException e) {
          e.printStackTrace();
        }


      }
    }

  }

  public void execute(String fName, ScopeImpl parent, ScopeImpl functionScope)
      throws CloneNotSupportedException {
    if (fName.startsWith("for")) {
      fName = fName.substring(0, fName.indexOf("("));
      ForScope forScope = (ForScope) parent.functions.get(fName);
      forScope.executeScript(fName, functionScope.getScopeVariables().clone());
    } else if (fName.startsWith("if")) {
//      fName = fName.substring("fcall ".length());
      fName = fName.substring(0, fName.indexOf("("));
      IfScope ifScope = (IfScope) parent.functions.get(fName);
      ifScope.executeScript(fName, functionScope.getScopeVariables().clone());
    } else {
      executeScript(fName, functionScope.getScopeVariables().clone());
    }
  }

  @Override
  public void run() {
    CommandIO commandIO;
    printLine();
    String userInput = "";
    while (true) {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      try {
        userInput = prepareInput(br.readLine());
//        userInput = "$(add($(add(${i}, ${j})), 5));";
//        userInput = "sh /home/jv/Umlet/umlet.sh";
//        userInput = "sh echo \"dsadasdas\" > /home/jv/Umlet/cosi.txt";
        userInput = "./home/jv/Documents/Skola/Diplomova-Praca/Sources/LiteShell/src/test/resources/test1.lsh";
        commandIO = executor.execute(userInput, getScope());
        if (commandIO.getCommandOutput().isPresent()) {
          commandIO.getCommandOutput().get().forEach(System.out::println);
        }
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
    return in.startsWith("./") || in.startsWith("sh ") || in.startsWith("win") ? in
        : in.endsWith(";") ? "$(" + in.substring(0, in.length() - 1) + ");" : "$(" + in + ");";
  }

  protected void loadSystemVariables() {
    scopeVariables.getStringMap().putAll(System.getenv());
    scopeVariables.getInitializedVariables().putAll(scopeVariables.getStringMap().keySet().stream()
        .collect(Collectors.toMap(a -> a, a -> Keyword.STRING)));
  }

  private void printLine() {
    System.out.print(getCurrentWorkingDirectory() + " > ");
  }


}
