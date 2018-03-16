package liteshell.receivers;

import java.util.Optional;

import liteshell.commands.ios.CommandOutput;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Receiver {
    CommandOutput executeCommand(String[] args, Optional<Scope> scope);
}
