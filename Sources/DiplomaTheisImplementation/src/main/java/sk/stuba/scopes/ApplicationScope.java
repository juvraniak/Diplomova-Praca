package sk.stuba.scopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Stack;

import sk.stuba.parser.CommandParser;
import sk.stuba.registration.Registrator;
import terminal.common.scopes.Scope;
import terminal.common.scopes.ScopeData;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class ApplicationScope implements Scope {

    private static final ApplicationScope INSTANCE = new ApplicationScope();
    private static final String INSTANCE_NAME = "Application";
    private static Stack callStack;
    private static Registrator registrator;
    private static CommandParser commandParser;

    private static String[] mockCommands = {"listdir /Users/jvraniak@sk.ibm.com/Desktop/test2/",
        "copy /Users/jvraniak@sk.ibm.com/Desktop/test/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt",
        "listdir /Users/jvraniak@sk.ibm.com/Desktop/test2/",
        "listdir /Users/jvraniak@sk.ibm.com/Desktop/test/",
        "move /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test/test1.txt",
        "listdir /Users/jvraniak@sk.ibm.com/Desktop/test/"};
    private final ScopeData scopeData = new ScopeData();

    private static void initialize() {
        registrator = new Registrator();
        commandParser = new CommandParser();
    }

    public static Registrator getRegistrator() {
        return registrator;
    }

    private static void printLine() {
        System.out.print("> ");
    }

    public static void run() throws IOException {
        initialize();

        for (String str : mockCommands) {

            commandParser.parse(str, INSTANCE);
        }
        printLine();

        String s = "";
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            s = br.readLine();
            if (s.equals("exit")) {
                break;
            }

            commandParser.parse(s, INSTANCE);
            printLine();
        }
    }

    @Override
    public terminal.common.scopes.ScopeData getScopeData() {
        return scopeData;
    }

    @Override
    public Scope getScope() {
        return INSTANCE;
    }

    @Override
    public void setScopeParameters(Map<String, String> parameters) {

    }

}
