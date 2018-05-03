package liteshell.executors;

import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Executor {

    void execute(String command, Scope scope);
}
