package liteshell.scopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import liteshell.exceptions.UnknownCommandException;
import liteshell.executors.Executor;
import liteshell.plugins.PluginFactory;
import liteshell.plugins.ShellPlugin;

/**
 * @author xvraniak@stuba.sk
 */

public class ApplicationScope extends ScopeImpl {

    public ApplicationScope(PluginFactory pluginFactory, Executor executor) {
        super("application", pluginFactory, executor);
    }

    private final ScopeData scopeData = new ScopeData();

    private static String[] mockCommands = {"ls /Users/jvraniak@sk.ibm.com/Desktop/test2/",
        "copy /Users/jvraniak@sk.ibm.com/Desktop/test/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt",
        "ls /Users/jvraniak@sk.ibm.com/Desktop/test2/",
        "ls /Users/jvraniak@sk.ibm.com/Desktop/test/",
        "move /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test/test1.txt",
        "ls /Users/jvraniak@sk.ibm.com/Desktop/test/"};

    private void printLine() {
        System.out.print("> ");
    }

    public void run() throws IOException {

//        for (String s : mockCommands) {
//            printLine();
//            System.out.println(s);
//            executor.execute(findShellPlugin(s), s, getScope());
//        }
        printLine();
        String s = "";
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            s = br.readLine();
            if (s.equals("exit")) {
                break;
            }
            try {
                executor.execute(findShellPlugin(s), s, getScope());
            } catch (UnknownCommandException ex) {
                ex.printStackTrace();
            }
            printLine();
        }
    }

    private Optional<ShellPlugin> findShellPlugin(String command) {
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
}
