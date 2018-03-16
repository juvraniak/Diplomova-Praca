package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;

import liteshell.commands.ios.CommandInput;
import liteshell.commands.ios.CommandOutput;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Command {

    CommandOutput execute(CommandInput commandInput, Optional<Scope> scope);

    String[] parseComand(Stream<String> var1);
}
