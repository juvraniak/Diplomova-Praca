package liteshell.executors;

import java.util.Optional;

import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Executor {

    void execute(Optional<ShellPlugin> plugin, String command, Scope scope);
}
