package liteshell.scopes;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xvraniak@stuba.sk
 */
@Slf4j
public class FunctionScope
//    extends ScopeImpl implements Runnable
{
//
//  private List<String> inputParameters;
//  private List<String> stack;
//  private String returnValue;
//
//
//  public FunctionScope(ShellClient shellClient) {
//    super("application", shellClient, null);
//    loadSystemVariables();
//  }
//
//  //mac execute
//    private static String[] mockCommands = {"ls /Users/jvraniak@sk.ibm.com/Desktop/test2/",
//        "copy /Users/jvraniak@sk.ibm.com/Desktop/test/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt",
//        "ls /Users/jvraniak@sk.ibm.com/Desktop/test2/",
//        "ls /Users/jvraniak@sk.ibm.com/Desktop/test/",
//        "move /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test/test1.txt",
//        "ls /Users/jvraniak@sk.ibm.com/Desktop/test/"};
//  //linux execute
//  private static String[] mockCommands = {"grep idea", "ls", "ls /home/jv/",
//      "ls /home/jv/ | grep ecli"};
//
//  private void printLine() {
//    System.out.print("> ");
//  }
//
//  @Override
//  public void run() {
//
//    for (String s : mockCommands) {
//      printLine();
//      System.out.println(s);
//      if (s.split("\\|").length > 1) {
//        executor.execute(getScope(), preparePlugins(s));
//      } else {
//        executor.execute(findShellPlugin(s), s, getScope());
//      }
//    }
//    printLine();
//    String s = "";
//    while (true) {
//      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//      try {
//        s = br.readLine();
//      } catch (IOException ex) {
//        log.error("Problem reading from command line:\n {}", ex.getMessage());
//      }
//      if (s.equals("exit")) {
//        System.exit(0);
//      }
//      //TODO : this should be only in executor
//      try {
//        if (s.split("\\|").length > 1) {
//          executor.execute(getScope(), preparePlugins(s));
//        } else {
//          executor.execute(findShellPlugin(s), s, getScope());
//        }
//      } catch (UnknownCommandException ex) {
//        System.out.println(ex.getMessage());
//      }
//      printLine();
//    }
//  }
//
//  private List<Pair<Optional<ShellPlugin>, String>> preparePlugins(String commands) {
//    String[] splittedCommands = commands.split(" \\| ");
//    return Arrays.stream(splittedCommands).map(c -> new Pair<>(findShellPlugin(c), c))
//        .collect(Collectors.toList());
//  }
//
//  private void loadSystemVariables() {
//    ScopeVariables variables = super.getScopeVariables();
//    variables.getStringMap().putAll(System.getenv());
//    variables.getInitializedVariables().addAll(variables.getStringMap().keySet());
//  }
}
