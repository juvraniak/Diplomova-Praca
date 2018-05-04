package liteshell.executors;

import liteshell.commands.ios.CommandOutput;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Executor {

    CommandOutput execute(String command, Scope scope);
}
