package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Command {

    CommandIO execute(CommandIO commandInput, Optional<Scope> scope);

    String[] parseComand(Stream<String> var1);
}
