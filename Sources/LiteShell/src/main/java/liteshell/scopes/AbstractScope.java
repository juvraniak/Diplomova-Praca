package liteshell.scopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
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
public class AbstractScope implements Scope, Runnable {

  @Setter
  protected List<String> inptutCommands;

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
    try {
      functionScope.setScopeVariables(scopeVariables.clone());
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    for (String command : functionScope.stack) {
      if (command.startsWith("$(")) {
        out = executor.execute(command, functionScope);
        if (command.contains("returnValue = ")) {
          Keyword key = functionScope.getScopeVariables().getInitializedVariables()
              .get("returnValue");
          scopeVariables.getInitializedVariables().put("returnValue", key);
          changeReturnValue(key, scopeVariables, functionScope.getScopeVariables());
        }
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
        String afterExecuteRetValue = "";
        if (split.length == 1) {
          fName = command.substring("fcall ".length());
        } else {
          fName = split[1];
          afterExecuteRetValue =
              "$(" + split[0].substring("fcall ".length()) + " = ${returnValue})";
          callParams = split[1].substring(split[1].indexOf("(") + 1, split[1].length() - 1);
          String[] splitCallParam = callParams.split(",");
          String callOnlyFName = fName.substring(0, fName.indexOf("("));
          ScopeImpl scope = parent.functions.get(callOnlyFName);
          List<Pair<String, String>> param = scope.inputParameters;
          for (int i = 0; i < callParams.split(",").length; i++) {
            this.getExecutor()
                .execute("$(" + param.get(i).getKey() + " " + param.get(i).getValue() + " = "
                    + splitCallParam[i] + ")", functionScope);
          }
        }

        try {
          execute(fName, parent, functionScope);
        } catch (CloneNotSupportedException e) {
          e.printStackTrace();
        }
        if (split.length > 1) {
          Keyword key = functionScope.getScopeVariables().getInitializedVariables()
              .get("returnValue");
//          try {
          functionScope.getExecutor().execute(afterExecuteRetValue, functionScope);
//            execute(afterExecuteRetValue, parent, functionScope);

        }
      }
    }

  }

  private void changeReturnValue(Keyword key, ScopeVariables scopeVariables,
      ScopeVariables calledFunctVariables) {
//    scopeVariables.getInitializedVariables().put("retunValue");
    System.out.println(returnValue);
    switch (key) {
      case INT:
        scopeVariables.getIntegerMap()
            .put("returnValue", calledFunctVariables.getIntegerMap().get("returnValue"));
        break;
      case DOUBLE:
        scopeVariables.getDoubleMap()
            .put("returnValue", calledFunctVariables.getDoubleMap().get("returnValue"));
        break;
      case BOOLEAN:
        scopeVariables.getBooleanMap()
            .put("returnValue", calledFunctVariables.getBooleanMap().get("returnValue"));
        break;
      case STRING:
        scopeVariables.getStringMap()
            .put("returnValue", calledFunctVariables.getStringMap().get("returnValue"));
        break;

    }
  }


  public void execute(String fName, ScopeImpl parent, ScopeImpl functionScope)
      throws CloneNotSupportedException {
    if (fName.startsWith("for")) {
      fName = fName.substring(0, fName.indexOf("("));
      ForScope forScope = (ForScope) parent.functions.get(fName);
      forScope.executeScript(fName, functionScope.getScopeVariables());
    } else if (fName.startsWith("if")) {
//      fName = fName.substring("fcall ".length());
      fName = fName.substring(0, fName.indexOf("("));
      IfScope ifScope = (IfScope) parent.functions.get(fName);
      ifScope.executeScript(fName, functionScope.getScopeVariables());
    } else {
      executeScript(fName, functionScope.getScopeVariables());
    }
  }

  public void runInputCommands() {
    try {
      inptutCommands.forEach(command -> {
        CommandIO commandIO = executor.execute(prepareInput(command), getScope());
        if (commandIO.getCommandOutput().isPresent() && commandIO.getReturnCode() == 0) {
          try {
            commandIO.getCommandOutput()
                .ifPresent(out -> System.out.println(out.reduce(String::concat).get()));
          } catch (Exception e) {

          }
        } else if (commandIO.getCommandErrorOutput().isPresent()) {
          try {
            commandIO.getCommandErrorOutput()
                .ifPresent(out -> System.out.println(out.reduce(String::concat).get()));
          } catch (Exception e) {

          }
        }
      });
    } catch (UnknownCommandException ex) {
      log.error("There was issue with following command: \n {}", ex.getMessage());
    }
  }

  @Override
  public void run() {
    CommandIO commandIO;
    printLine();
    String userInput = "";
    while (!userInput.equals("$(exit);")) {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      try {
        userInput = prepareInput(br.readLine());
        if (userInput.equals("$(exit);")) {
          System.exit(0);
        }
        Instant start = Instant.now();
        commandIO = executor.execute(userInput, getScope());
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));

        if (commandIO.getCommandOutput().isPresent() && commandIO.getReturnCode() == 0) {
          try {
            commandIO.getCommandOutput()
                .ifPresent(out -> System.out.println(out.reduce(String::concat).get()));
          } catch (Exception e) {

          }
        } else if (commandIO.getCommandErrorOutput().isPresent()) {
          try {
            commandIO.getCommandErrorOutput()
                .ifPresent(out -> System.out.println(out.reduce(String::concat).get()));
          } catch (Exception e) {

          }
        }
      } catch (UnknownCommandException ex) {
        log.error("There was issue with following command: \n {}", ex.getMessage());
      } catch (IOException ex) {
        log.error("Problem reading from command line:\n {}", ex.getMessage());
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
