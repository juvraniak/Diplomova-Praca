package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;

import liteshell.commands.ios.CommandInput;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class VariableCommand implements Command {

    @Override
    public CommandOutput execute(CommandInput commandInput, Optional<Scope> scope) {
        return new DefaultOutput();
    }

    @Override
    public String[] parseComand(Stream<String> var1) {
        return new String[0];
    }
}
