package sk.stuba.scopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Stack;

import sk.stuba.parser.ParserImpl;
import sk.stuba.plugins.PluginFactory;
import terminal.common.scopes.Scope;
import terminal.common.scopes.ScopeData;


/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class ScopeImpl implements Scope {

    private static String scopeName;
    private static Stack callStack;
    private static PluginFactory pluginFactory;
    private static ParserImpl commandParser;

    private static String[] mockCommands = {"listdir /Users/jvraniak@sk.ibm.com/Desktop/test2/",
        "copy /Users/jvraniak@sk.ibm.com/Desktop/test/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt",
        "listdir /Users/jvraniak@sk.ibm.com/Desktop/test2/",
        "listdir /Users/jvraniak@sk.ibm.com/Desktop/test/",
        "move /Users/jvraniak@sk.ibm.com/Desktop/test2/test.txt /Users/jvraniak@sk.ibm.com/Desktop/test/test1.txt",
        "listdir /Users/jvraniak@sk.ibm.com/Desktop/test/"};
    private final ScopeData scopeData = new ScopeData();

    private static void initialize() {
        pluginFactory = PluginFactory.init();
        commandParser = new ParserImpl();
    }


    private void printLine() {
        System.out.print("> ");
    }

    public void run() throws IOException {
        initialize();

//        for (String str : mockCommands) {
//
//            commandParser.parse(str, this);
//        }
        printLine();

        String s = "";
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            s = br.readLine();
            if (s.equals("exit")) {
                break;
            }

            commandParser.parse(s, this);
            printLine();
        }
    }

    @Override
    public terminal.common.scopes.ScopeData getScopeData() {
        return scopeData;
    }

    @Override
    public Scope getScope() {
        return this;
    }

    @Override
    public void setScopeParameters(Map<String, String> parameters) {

    }

}
