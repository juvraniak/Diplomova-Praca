package liteshell.executors;

import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Executor {

    CommandIO execute(String command, Scope scope);
}
