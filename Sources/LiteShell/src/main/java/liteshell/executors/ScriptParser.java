package liteshell.executors;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.util.Pair;
import liteshell.exceptions.MethodMissingEception;
import liteshell.plugins.PluginFactory;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeImpl;
import liteshell.utils.ShellClient;
import liteshell.utils.StringUtils;


/**
 * @author xvraniak@stuba.sk
 */

public class ScriptParser {

  private static final String BLANK_SPACE = " ";
  private static final String COMMENT = "#";
  private static final String PIPE = "\\|";
  private static final String INCLUDE = "include";
  private List<String> CONTENT = new ArrayList<>();
  private Map<String, ScopeImpl> listOfScopes = new HashMap<>();

  private ShellClient shellClient;


  public ScriptParser(String path) throws MethodMissingEception {
    this.shellClient = ShellClient.getInstance();
    loadScript(path);
  }

  public void parse(String scriptPath) {
    ScopeImpl scriptScope = createNewScope("main");
    boolean isOverride = false;


    StringBuilder sb = new StringBuilder();
    String line;

    for (int i = 0; i < CONTENT.size(); ) {
      line = CONTENT.get(i);
      if (line.trim().startsWith("@Override")) {
        isOverride = true;
        i++;
      } else if (line.trim().startsWith("function")) {
        i = handleFunction(i, scriptScope, isOverride);
        isOverride = false;
      } else if (line.endsWith("\\")) {
        sb.append(line.substring(0, line.length() - 1));
        i++;
      } else if (line.endsWith(";")) {
        //for noW it may be only global variables
        sb.append(line.replace(";", "")).append(");");
        scriptScope.addCommand("$(" + sb.toString().trim());
        i++;
      }
    }

    System.out.println("tu");
    scriptScope.setFunctions(listOfScopes);
    scriptScope.executeScript();

  }

  private int handleFunction(int currentLine, Scope parrentScope, boolean isOverride) {
    String line = CONTENT.get(currentLine).trim();
    StringBuilder sb = new StringBuilder();
    String parameters = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
    boolean correctEnd = line.endsWith("{");
    String functionLineStr = line.substring("function ".length(), line.indexOf("("));

    String[] functionLine = StringUtils.removeEmptyStrings(functionLineStr.split(BLANK_SPACE));

    String returnType = functionLine[0];
    String functionName = functionLine[1];
    ScopeImpl functionScope = (ScopeImpl) createNewScope(functionName, parrentScope);
    functionScope.setReturnType(returnType);
    List<Pair<String, String>> input = handleInputParam(parameters);
    functionScope.setInputParameters(input);
    functionScope.setOverride(isOverride);
    for (int i = currentLine + 1; i < CONTENT.size(); i++) {
      line = CONTENT.get(i);
      if (line.endsWith(";")) {
        sb.append(line.trim());
        sb.deleteCharAt(sb.length() - 1);
        if (isFunctionCall(sb.toString())) {
          functionScope.addCommand(sb.toString());
        } else {
          functionScope.addCommand("$(" + sb.toString() + ");");
        }
        sb.delete(0, sb.toString().length());
      } else if (line.endsWith("\\")) {
        sb.append(line.substring(0, line.length() - 1));

//        if (line.length() > 0 && !line.endsWith(" ")) {
//          sb.append(" ");
//        }
      } else if (line.trim().endsWith("}")) {
        if (line.trim().length() != 1) {
          //TODO: handle the command before }
          //or maybe it will be prohibited and throw exception...
        }
        currentLine = i + 1;
        break;
      }
    }
    if (isOverride || listOfScopes.get(functionName) == null) {
      listOfScopes.put(functionName, functionScope);
    }
    return currentLine;
  }

  private boolean isFunctionCall(String call) {
    boolean isFunction = false;
    List<String> collect = listOfScopes.keySet().stream().filter(str -> call.contains(str + "("))
        .collect(Collectors.toList());
    if (!collect.isEmpty()) {
      isFunction = true;
    }
    return isFunction;
  }

  private List<Pair<String, String>> handleInputParam(String parameters) {
    if (parameters.isEmpty()) {
      return new ArrayList<>();
    }
    String[] splittedParams = StringUtils.removeEmptyStrings(parameters.split(","));
    List<Pair<String, String>> in = new ArrayList<>();
    for (String s : splittedParams) {
      String[] split = s.split(BLANK_SPACE);
      in.add(new Pair<>(split[0], split[1]));
    }
    return in;
  }

  private void loadScript(String path) throws MethodMissingEception {
    Set<String> allIncludes = new HashSet<>();
    try (Stream<String> lines = Files.lines(Paths.get(path), Charset.defaultCharset())) {
      PluginFactory pluginFactory = shellClient.getPluginFactory();
      CONTENT = lines
          .filter(line -> !line.trim().startsWith(COMMENT))
          .filter(line -> line.trim().startsWith(INCLUDE))
          .filter(line -> !line.isEmpty())
          .collect(Collectors.toList());
      String firstLine = lines.findFirst().get();
      if (firstLine.startsWith("#!")) {
        CONTENT.add(0, firstLine);
      }
      List<String> plugins = CONTENT.stream().filter(line -> line.startsWith("use"))
          .collect(Collectors.toList());

      List<String> include = CONTENT.stream().filter(line -> line.trim().startsWith(INCLUDE))
          .collect(Collectors.toList());

      List<String> functions = CONTENT.stream()
          .filter(line -> line.startsWith("function"))
          .map(line -> {
            String functionLineStr = line.substring("function ".length(), line.indexOf("("));
            return StringUtils.removeEmptyStrings(functionLineStr.trim().split(BLANK_SPACE))[1];
          })
          .collect(Collectors.toList());
      functions.forEach(f -> listOfScopes.put(f, null));
      if (!listOfScopes.containsKey("main")) {
        throw new MethodMissingEception("main");
      }

      plugins.forEach(plugin -> pluginFactory.changePlugin(plugin));

      CONTENT.removeAll(plugins);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private ScopeImpl createNewScope(String name) {
    return new ScopeImpl(name, shellClient, null);
  }

  private Scope createNewScope(String name, Scope parrent) {
    return new ScopeImpl(name, shellClient, parrent);
  }

}