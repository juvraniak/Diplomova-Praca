package liteshell.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import liteshell.commands.Command;
import liteshell.commands.ListDirectoryCommand;
import liteshell.parsers.CommandParser;
import liteshell.parsers.FunctionParser;
import liteshell.parsers.ListDirParser;

/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class ListDirPlugin implements ShellPlugin {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "ls", createListDirRegex());
    }

    @Override
    public Command getCommand() {
        return new ListDirectoryCommand();
    }

    @Override
    public boolean shouldPrint() {
        return true;
    }

    @Override
    public Optional<CommandParser> getCommandParser() {
        return Optional.of(new ListDirParser());
    }

    @Override
    public Optional<FunctionParser> getFunctionParser() {
        return Optional.empty();
    }

    private Optional<List<String>> createListDirRegex() {
        return Optional.of(Arrays.asList("ls"));
    }
}
