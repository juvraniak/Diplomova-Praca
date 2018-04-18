package liteshell.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import liteshell.commands.Command;
import liteshell.commands.CopyCommand;
import liteshell.parsers.CommandParser;
import liteshell.parsers.FunctionParser;
import liteshell.parsers.RemoveParser;


/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class RemovePlugin implements ShellPlugin {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "rm", createRegex());
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
        return Optional.of(new RemoveParser());
    }

    @Override
    public Optional<FunctionParser> getFunctionParser() {
        return Optional.empty();
    }

    private Optional<List<String>> createRegex() {
        return Optional.of(Arrays.asList("rm\\s{1}.+;?"));
    }
}
