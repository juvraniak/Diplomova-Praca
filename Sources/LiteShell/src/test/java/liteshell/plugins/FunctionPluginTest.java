package liteshell.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import liteshell.commands.Command;
import liteshell.commands.ios.DefaultInput;
import liteshell.executors.ExecutorImpl;
import liteshell.scopes.ScopeImpl;
import liteshell.test.PluginTest;

/**
 * @author xvraniak@stuba.sk
 */

public class FunctionPluginTest implements PluginTest {

    FunctionPlugin functionPlugin = new FunctionPlugin();

    @Test
    @Override
    public void regexTest() {
        Optional<List<String>> patterns = functionPlugin.getInfo().getMatcher();
        Assert.assertTrue("Matcher list is empty!", patterns.isPresent());
        List<String> correctFunction = Arrays.asList("function void aaa3a(void a1, int a, list c){\n"
                + "command;\ncommand;\n"
                + "}",
            "function int funct2(void a1, int a, list c){\n"
                + "line 1;\nline 2;\n"
                + "}\n"
                + "\n");
        List<String> incorrectFunction = Arrays.asList("function void aaa3a(void a1, int a, list c)\n"
                + "command;\ncommand;\n"
                + "}",
            "function int funct2(void a1, int a, list c){\n"
                + "line 1\nline 2;\n"
                + "}\n"
                + "\n");
        assertRegex(patterns, correctFunction, incorrectFunction);
    }

    @Test
    @Override
    public void commandTest() {
        Command command = functionPlugin.getCommand();
        command.execute(new DefaultInput(Stream.of("")), Optional.of(new ScopeImpl("test", PluginFactory.init(), new ExecutorImpl())));
    }
}
