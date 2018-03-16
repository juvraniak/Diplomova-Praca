package liteshell;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import liteshell.executors.Executor;
import liteshell.executors.ExecutorImpl;
import liteshell.plugins.PluginFactory;
import liteshell.scopes.ApplicationScope;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {

    public static void main(String[] args) {
        Stream<String> cmd = Stream.of("copy com com");
        cmd.forEach(System.out::println);
        PluginFactory pluginFactory = PluginFactory.init();
        Executor executor = new ExecutorImpl();

        Scope application = new ApplicationScope(pluginFactory, executor);

        try {
            ((ApplicationScope) application).run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String s = "function void aaa3a(void a1, int a, list c){\n"
            + "command;\ncommand\n"
            + "}"
            + "\n"
            + "function int funct2(void a1, int a, list c){\n"
            + "line 1\n"
            + "line 2;\n"
            + "}\n"
            + "\n";
        String types = "(void|int|double|list|map)";
        String charsAndNumbers = "[a-zA-Z1-9]";
        String line = "((\t*.*[;]\n){0,1})*";
        String[] lines = s.split("\n");
        Pattern p = Pattern.compile(
            "function " + types + " \\D.{0,20}[(](" + types + " \\D" + charsAndNumbers + "{0,20})*(,\\s*" + types + " \\D" + charsAndNumbers
                + "{0,20})*[)]\\s*[{]\\s*\n" + line + "[}]\n*");
        match(s, p);
//        for (String str : lines) {
//            match(str, p);
//        }
//        s = "command | command par1 | command par1 par2 | command par1 par2 par 3;";
//        p = Pattern.compile("^.+\\s*([|]\\s*.+)+[;]$");
//        match(s, p);
        s = "command;\ncommand;\n";
        p = Pattern.compile(line);
//        match(s, p);
    }

    public static void match(String str, Pattern p) {
        Matcher m = p.matcher(str);
        System.out.print(str + " - ");
        if (m.matches()) {
            System.out.println("ok");
        } else {
            System.out.println("not ok");
        }
    }
}
