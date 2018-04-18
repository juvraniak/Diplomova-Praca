package liteshell.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import liteshell.commands.Command;
import liteshell.commands.CopyCommand;
import liteshell.parsers.CommandParser;
import liteshell.parsers.CopyParser;
import liteshell.parsers.FunctionParser;


/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class CopyPlugin implements ShellPlugin {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "copy", createCopyRegex());
    }

    @Override
    public Command getCommand() {
        return new CopyCommand();
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    public Optional<CommandParser> getCommandParser() {
        return Optional.of(new CopyParser());
    }

    @Override
    public Optional<FunctionParser> getFunctionParser() {
        return Optional.empty();
    }

    private Optional<List<String>> createCopyRegex() {
        return Optional.of(Arrays.asList("copy\\s{1}.+\\s{1}.+;"));
    }
}
