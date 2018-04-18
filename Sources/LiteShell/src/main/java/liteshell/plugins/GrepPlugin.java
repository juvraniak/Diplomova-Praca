package liteshell.plugins;


import java.util.Optional;
import liteshell.commands.Command;
import liteshell.commands.GrepCommand;
import liteshell.parsers.CommandParser;
import liteshell.parsers.FunctionParser;

/**
 * @author xvraniak@stuba.sk
 */

public class GrepPlugin implements ShellPlugin {
    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "grep", Optional.empty());
    }

    @Override
    public Command getCommand() {
        return new GrepCommand();
    }

    @Override
    public boolean shouldPrint() {
        return true;
    }

    @Override
    public Optional<CommandParser> getCommandParser() {
        return Optional.empty();
    }

    @Override
    public Optional<FunctionParser> getFunctionParser() {
        return Optional.empty();
    }
}
