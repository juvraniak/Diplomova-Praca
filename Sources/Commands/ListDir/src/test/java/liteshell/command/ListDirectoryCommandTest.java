package liteshell.command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import liteshell.commands.Command;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
import liteshell.plugins.ListDirPlugin;
import liteshell.test.PluginTest;

public class ListDirectoryCommandTest implements PluginTest{

    ListDirPlugin copyPlugin = new ListDirPlugin();

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

        CommandOutput out = lsCommand.execute(DefaultInput.of(Stream.of("ls "+ System.getProperty("user.dir"))), Optional.empty());
        Assert.assertTrue(out.getCommandErrorOutput().isPresent());
        Assert.assertTrue(out.getCommandOutput().isPresent());
        Assert.assertTrue(out.getCommandErrorOutput().get().collect(Collectors.toList()).size() == 0);

//        out.getCommandOutput().get().forEach(System.out::println);
        out = lsCommand.execute(DefaultInput.of(Stream.of("ls")), Optional.empty());
        out.getCommandOutput().get().forEach(System.out::println);
    }

}
