package liteshell.executors;

import java.util.List;
import java.util.Optional;

import javafx.util.Pair;
import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public interface Executor {

    void execute(Optional<ShellPlugin> plugin, String command, Scope scope);

    void execute(Scope scope, List<Pair<Optional<ShellPlugin>, String>> plugins);
}
