package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;

import liteshell.commands.ios.CommandInput;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class PipeCommand implements Command {


    @Override
    public void execute(CommandInput commandInput, Optional<Scope> scope) {

    }

    @Override
    public String[] parseComand(Stream<String> var1) {
        return new String[0];
    }
}
