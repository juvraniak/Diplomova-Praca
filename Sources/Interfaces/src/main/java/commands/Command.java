package commands;

import commandcommons.CommandOutput;
import commandcommons.CommandInput;
import scopes.ApplicationScope;

@FunctionalInterface
public interface Command {
    CommandOutput fireUpCommand(CommandInput input, ApplicationScope scope);
}
