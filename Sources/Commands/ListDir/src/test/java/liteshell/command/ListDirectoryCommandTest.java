package liteshell.command;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import liteshell.commands.Command;
import liteshell.commands.ios.CommandIO;
import liteshell.commands.ios.DefaultCommadIO;
import liteshell.test.PluginTest;
import liteshell.v1.plugins.ListDirPlugin;
import org.junit.Assert;
import org.junit.Test;

public class ListDirectoryCommandTest implements PluginTest{

    ListDirPlugin copyPlugin = new ListDirPlugin();

    @Test
    @Override
    public void commandTest() {
        Command lsCommand = copyPlugin.getCommand();
        Assert.assertNotNull(lsCommand);

        CommandIO out = lsCommand
            .execute(
                DefaultCommadIO.of(Optional.of(Stream.of("ls " + System.getProperty("user.dir")))),
                Optional.empty());
        Assert.assertTrue(out.getCommandErrorOutput().isPresent());
        Assert.assertTrue(out.getCommandOutput().isPresent());
        Assert.assertTrue(out.getCommandErrorOutput().get().collect(Collectors.toList()).size() == 0);

        out = lsCommand.execute(DefaultCommadIO.of(Optional.of(Stream.of("ls"))), Optional.empty());
        out.getCommandOutput().get().forEach(System.out::println);
    }

}
