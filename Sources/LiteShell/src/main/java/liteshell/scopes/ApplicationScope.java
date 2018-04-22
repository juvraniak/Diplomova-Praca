package liteshell.scopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.util.Pair;
import liteshell.Client;
import liteshell.exceptions.UnknownCommandException;
import liteshell.plugins.ShellPlugin;

/**
 * @author xvraniak@stuba.sk
 */

public class ApplicationScope extends ScopeImpl implements Runnable {

  public ApplicationScope(Client client) {
    super("application", client);
    loadSystemVariables();
  }



  private final ScopeVariables scopeData = new ScopeVariables();
  //mac execute
//    private static String[] mockCommands = {"ls /Users/jvraniak@sk.ibm.com/Desktop/test2/",
//        "copy /Users/jvraniak@sk.ibm.com/Desktop/test/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt",
//        "ls /Users/jvraniak@sk.ibm.com/Desktop/test2/",
//        "ls /Users/jvraniak@sk.ibm.com/Desktop/test/",
//        "move /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test/test1.txt",
//        "ls /Users/jvraniak@sk.ibm.com/Desktop/test/"};
  //linux execute
  private static String[] mockCommands = {"grep idea", "ls", "ls /home/jv/",
      "ls /home/jv/ | grep ecli"};

  private void printLine() {
    System.out.print("> ");
  }

  @Override
  public void run() {

    for (String s : mockCommands) {
      printLine();
      System.out.println(s);
      if (s.split("\\|").length > 1) {
        executor.execute(getScope(), preparePlugins(s));
      } else {
        executor.execute(findShellPlugin(s), s, getScope());
      }
    }
    printLine();
    String s = "";
    while (true) {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      try {
        s = br.readLine();
      } catch (IOException ex) {
        System.out.println("Problem reading from command line");
      }
      if (s.equals("exit")) {
        System.exit(0);
      }
      try {
        if (s.split("\\|").length > 1) {
          executor.execute(getScope(), preparePlugins(s));
        } else {
          executor.execute(findShellPlugin(s), s, getScope());
        }
      } catch (UnknownCommandException ex) {
        System.out.println(ex.getMessage());
      }
      printLine();
    }
  }

  private List<Pair<Optional<ShellPlugin>, String>> preparePlugins(String commands) {
    String[] splittedCommands = commands.split(" \\| ");
    return Arrays.stream(splittedCommands).map(c -> new Pair<>(findShellPlugin(c), c))
        .collect(Collectors.toList());
  }

  private void loadSystemVariables() {
    ScopeVariables variables = this.getScopeVariables();
    variables.getStringMap().putAll(System.getenv());
    variables.getInitializedVariables().addAll(variables.getStringMap().keySet());
  }

  @Override
  public ScopeVariables getScopeVariables() {
    return null;
  }
}
