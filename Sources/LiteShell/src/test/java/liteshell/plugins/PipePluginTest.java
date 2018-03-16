package liteshell.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import liteshell.test.PluginTest;

/**
 * @author xvraniak@stuba.sk
 */

public class PipePluginTest implements PluginTest {

    private PipePlugin pipePlugin = new PipePlugin();

    @Test
    @Override
    public void regexTest() {
        Optional<List<String>> patterns = pipePlugin.getInfo().getMatcher();
        Assert.assertTrue("Matcher list is empty!", patterns.isPresent());
        List<String> correctCommand = Arrays.asList("command | command par1 | command par1 par2 | command par1 par2 par 3;",
            "command|command par1|command par1 par2|command par1 par2 par 3;");
        List<String> incorrectCommand = Arrays.asList("command", "command;", ";command | command par1 | command par1 par2 | command par1 par2 par 3",
            "command | command par1 | command par1 par2 | command par1 par2 par 3");

        assertRegex(patterns, correctCommand, incorrectCommand);
    }

    @Test
    @Override
    public void commandTest() {

    }

}
