package liteshell.executors;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.util.Pair;
import liteshell.exceptions.PluginNotSupportedException;
import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeImpl;
import liteshell.utils.ShellClient;


/**
 * @author xvraniak@stuba.sk
 */

public class ScriptParser {

  private static final String BLANK_SPACE = " ";
  private static final String COMMENT = "//";
  private static final String PIPE = "\\|";

  private ShellClient shellClient;

  public ScriptParser() {
    this.shellClient = ShellClient.getInstance();
  }

  public void parse(String scriptPath) {

    Scope scriptScope = new ScopeImpl(scriptPath, shellClient);
//    scriptScope.getScope().
    StringBuilder sb = new StringBuilder();
    try (Stream<String> lines = Files.lines(Paths.get(scriptPath), Charset.defaultCharset())) {
      lines
          .filter(line -> !line.trim().startsWith("//"))
          .forEachOrdered(line -> {
            if (line.endsWith(";")) {
              try {
                process(sb.append(line.trim()).toString(), scriptScope);
              } catch (PluginNotSupportedException e) {
                e.printStackTrace();
              }
              sb.delete(0, sb.toString().length());
            } else {

              sb.append(line);

              if (line.length() > 0 && !line.endsWith(" ")) {
                sb.append(" ");
              }
            }
          });

      System.out.println("tu");
      scriptScope.executeScript();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void process(String line, Scope scope) throws PluginNotSupportedException {
    System.out.println("in process");
    System.out.println(line);

    //check if line is a pipe
    String[] tokens = line.split(PIPE);
    Optional<ShellPlugin> plugin = scope.findShellPlugin(line);

    if (plugin.isPresent()) {
      Pair<ShellPlugin, String> pair = new Pair<>(plugin.get(),
          line.substring(0, line.length() - 1));
      scope.addCommand(pair);
    } else {
      throw new PluginNotSupportedException("Did not find plugin : " + line.split(" ")[0]);
    }


  }

}
