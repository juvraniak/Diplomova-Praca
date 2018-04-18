package liteshell.plugins;

import java.util.Optional;
import liteshell.commands.Command;
import liteshell.parsers.CommandParser;
import liteshell.parsers.FunctionParser;

/**
 * @author xvraniak@stuba.sk
 */

public interface ShellPlugin {

    PluginMeta getInfo();

    Command getCommand();

    boolean shouldPrint();

    Optional<CommandParser> getCommandParser();

    Optional<FunctionParser> getFunctionParser();
}
