package liteshell.executors;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.util.Pair;
import liteshell.exceptions.MethodMissingEception;
import liteshell.exceptions.PluginNotSupportedException;
import liteshell.plugins.PluginFactory;
import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeImpl;
import liteshell.utils.ShellClient;
import liteshell.utils.StringUtils;


/**
 * @author xvraniak@stuba.sk
 */

public class ScriptParser {

  private static final String BLANK_SPACE = " ";
  private static final String COMMENT = "//";
  private static final String PIPE = "\\|";
  private List<String> CONTENT = new ArrayList<>();
  private Map<String, Scope> listOfScopes = new HashMap<>();

  private ShellClient shellClient;


  public ScriptParser(String path) throws MethodMissingEception {
    this.shellClient = ShellClient.getInstance();
    loadScript(path);
  }

  public void parse(String scriptPath) {
    Scope scriptScope = createNewScope("main");


    StringBuilder sb = new StringBuilder();
    String line;

    for (int i = 0; i < CONTENT.size(); ) {
      line = CONTENT.get(i);
      if (line.trim().startsWith("function")) {
        i = handleFunction(i);
      }
    }

    System.out.println("tu");
    scriptScope.executeScript();

  }

  private int handleFunction(int currentLine) {
    String line = CONTENT.get(currentLine).trim();
    StringBuilder sb = new StringBuilder();
    String parameters = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
    boolean correctEnd = line.endsWith("{");
    String functionLineStr = line.substring("function ".length(), line.indexOf("("));

    String[] functionLine = StringUtils.removeEmptyStrings(functionLineStr.split(BLANK_SPACE));

    String returnType = functionLine[0];
    String functionName = functionLine[1];
    Scope functionScope = createNewScope(functionName);

    for (int i = currentLine + 1; i < CONTENT.size(); i++) {
      line = CONTENT.get(i);
      if (line.endsWith(";")) {
        try {
          process(sb.append(line.trim()).toString(), functionScope);
        } catch (PluginNotSupportedException e) {
          e.printStackTrace();
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
    listOfScopes.put(functionName, functionScope);
    return currentLine;
  }

  private void process(String line, Scope scope) throws PluginNotSupportedException {
    System.out.println("in process");
    System.out.println(line);
    String[] splited = StringUtils.removeEmptyStrings(line.trim().split(BLANK_SPACE));
    String processedCommand = line;

    if (splited.length > 1) {
      String processedCommand = processParameters(splited);
    }
    //check if line is a pipe
    String[] tokens = line.split(PIPE);
    Optional<ShellPlugin> plugin = scope.findShellPlugin(line);

    if (plugin.isPresent()) {
      Pair<ShellPlugin, String> pair = new Pair<>(plugin.get(),
          line.substring(0, line.length() - 1));
      scope.addCommand(pair);
    } else {
      throw new PluginNotSupportedException("Did not find plugin : " + line.split(BLANK_SPACE)[0]);
    }


  }

  private String processParameters(String[] splited) {
    String command = splited[0];

  }

  private void loadScript(String path) throws MethodMissingEception {
    try (Stream<String> lines = Files.lines(Paths.get(path), Charset.defaultCharset())) {
      PluginFactory pluginFactory = shellClient.getPluginFactory();
      CONTENT = lines
          .filter(line -> !line.trim().startsWith("//"))
          .filter(line -> !line.isEmpty())
          .collect(Collectors.toList());

      List<String> plugins = CONTENT.stream().filter(line -> line.startsWith("use"))
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

  private Scope createNewScope(String name) {
    return new ScopeImpl(name, shellClient);
  }

}


class ScriptScope {

  //map of shell plugins + their command - it has executeScript therefore shell plugin for each for cycle etc instead of scope?
  private Map<String, Scope> functions;
  //i need something that will hold

}