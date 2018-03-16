package liteshell.command;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import liteshell.commands.Command;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
import liteshell.plugins.PluginImpl;
import liteshell.test.PluginTest;

public class ListDirectoryCommandTest implements PluginTest{

    PluginImpl copyPlugin = new PluginImpl();

    @Test
    @Override
    public void regexTest() {
        Optional<List<String>> patterns = copyPlugin.getInfo().getMatcher();
        Assert.assertTrue("Matcher list is empty!", patterns.isPresent());
        List<String> correctCommand = Arrays.asList("ls");
        List<String> incorrectCommand = Arrays.asList("lsx");

        assertRegex(patterns, correctCommand, incorrectCommand);
    }
    @Test
    @Override
    public void commandTest() {
        Command lsCommand = copyPlugin.getCommand();
        Assert.assertNotNull(lsCommand);

        CommandOutput out = lsCommand.execute(new DefaultInput(Stream.of("ls "+ System.getProperty("user.dir"))), Optional.empty());
        Assert.assertTrue(!out.getCommandErrorOutput().isPresent());
        Assert.assertTrue(out.getCommandOutput().isPresent());

        out.getCommandOutput().get().forEach(System.out::println);
    }

}