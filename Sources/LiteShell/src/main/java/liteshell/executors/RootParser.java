package liteshell.executors;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.util.Pair;
import liteshell.exceptions.MethodMissingEception;
import liteshell.keywords.Keyword;
import liteshell.parser.Parser;
import liteshell.plugins.PluginFactory;
import liteshell.scopes.ForScope;
import liteshell.scopes.IfScope;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeImpl;
import liteshell.utils.StringUtils;


/**
 * @author xvraniak@stuba.sk
 */

public class RootParser implements Parser {

  private final ScopeImpl scriptScope = createNewScope("script");
  private List<String> CONTENT = new ArrayList<>();

  public RootParser(String path) throws MethodMissingEception {
    loadScript(path);
  }

  public void parse() {

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

    scriptScope.setFunctions(listOfScopes);
    scriptScope.executeScript("main()");

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
    functionScope.setReturnType(Keyword.getKeywordForString(returnType));
    List<Pair<String, String>> input = handleInputParam(parameters);
    input.stream().filter(p -> !p.getKey().equals("void"))
        .forEach(p -> initInputVariable(p, functionScope));
    functionScope.setInputParameters(input);
    functionScope.setOverride(isOverride);
    for (int i = currentLine + 1; i < CONTENT.size(); i++) {
      line = CONTENT.get(i);
      if (line.endsWith(";")) {
        if (line.trim().startsWith("return")) {
          line = functionScope.assignReturnValue(line, functionScope.getReturnType());
        }
        sb.append(line.trim());
        sb.deleteCharAt(sb.length() - 1);
        if (isFunctionCall(sb.toString())) {
          functionScope.addCommand("fcall " + sb.toString());
        } else {
          functionScope.addCommand("$(" + sb.toString().trim() + ");");
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
      } else if (line.trim().startsWith("if(")) {
        String ifName = "if" + i;
        IfScope newIf = new IfScope(ifName, (ScopeImpl) functionScope.getParent(), functionScope,
            CONTENT, i);
        i = newIf.preProcess();
        listOfScopes.put(ifName, newIf);
        functionScope.addCommand("fcall " + ifName + "()");
      } else if (line.trim().startsWith("for(")) {
        String forName = "for" + i;
        ForScope newFor = new ForScope(forName, (ScopeImpl) functionScope.getParent(),
            functionScope, CONTENT,
            i);
        i = newFor.preProcess();
        listOfScopes.put(forName, newFor);
        functionScope.addCommand("fcall " + forName + "()");
      }
    }
    if (isOverride || listOfScopes.get(functionName) == null) {
      listOfScopes.put(functionName, functionScope);
    }
    return currentLine;
  }

  private void initInputVariable(Pair<String, String> p, ScopeImpl scope) {
    scope.getExecutor().execute("$(" + p.getKey() + " " + p.getValue() + ");", scope);
  }

  private void loadScript(String path) throws MethodMissingEception {
    Map<String, Boolean> allIncludes;
    try (Stream<String> lines = Files.lines(Paths.get(path), Charset.defaultCharset())) {
      PluginFactory pluginFactory = shellClient.getPluginFactory();
      CONTENT = lines
//          .filter(line -> line.trim().startsWith(INCLUDE))
          .filter(line -> !line.isEmpty())
          .collect(Collectors.toList());
      String firstLine = CONTENT.get(0);
      CONTENT = CONTENT.stream().filter(line -> !line.trim().startsWith(COMMENT))
          .collect(Collectors.toList());
      if (firstLine.startsWith("#!")) {
        CONTENT.add(0, firstLine);
      }
      List<String> plugins = CONTENT.stream().filter(line -> line.startsWith("use"))
          .collect(Collectors.toList());

      allIncludes = CONTENT.stream().filter(line -> line.trim().startsWith(INCLUDE))
          .collect(Collectors.toMap(i -> i, i -> false));

      allIncludes.forEach((k, v) -> CONTENT.remove(k));
      List<String> loadedInputs = handleIncludes(allIncludes);

      CONTENT.addAll(loadedInputs);

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

  private List<String> handleIncludes(Map<String, Boolean> allIncludes) {
    List<String> loadedRows = new ArrayList<>();
    for (Entry<String, Boolean> set : allIncludes.entrySet()) {
      String k = set.getKey();
      Boolean v = set.getValue();
      if (allIncludes.get(k) != true) {
        String path = k.substring(k.indexOf("(") + 1, k.length() - 2);
        try (Stream<String> lines = Files.lines(Paths.get(path), Charset.defaultCharset())) {
          //load all lines
          List<String> newLines = lines
              .filter(line -> !line.isEmpty())
              .collect(Collectors.toList());
          //find and add includes that are not already in map
          newLines.stream().filter(line -> line.trim().startsWith(INCLUDE))
              .collect(Collectors.toList()).forEach(line -> {
            if (!allIncludes.containsKey(line)) {
              allIncludes.put(line, false);
            }
          });
          //delete comments
          newLines = newLines.stream().filter(line -> !line.trim().startsWith(COMMENT))
              .filter(line -> !line.trim().startsWith(INCLUDE))
              .collect(Collectors.toList());
          //add all loaded lines
          loadedRows.addAll(newLines);
          //mark complete loading
          allIncludes.put(k, true);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    List<Boolean> collect = allIncludes.values().stream().filter(v -> !v)
        .collect(Collectors.toList());
    if (!collect.isEmpty()) {
      loadedRows.addAll(handleIncludes(allIncludes));
    }
    return loadedRows;
  }


}