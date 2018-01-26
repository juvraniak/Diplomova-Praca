package receivers;

import commandcommons.CommandOutput;
import scopes.ApplicationScope;

@FunctionalInterface
public interface Receiver {
    CommandOutput receiveAndExecute(String[] parsedCommand, ApplicationScope scope);
}
