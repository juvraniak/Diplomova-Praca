package liteshell.scopes;

import java.util.Optional;
import javafx.util.Pair;
import liteshell.plugins.ShellPlugin;

/**
 * @author xvraniak@stuba.sk
 */

public interface Scope {

  ScopeVariables getScopeData();

    default Scope getScope() {
        return this;
    }

    String getCurrentWorkingDirectory();

  Optional<ShellPlugin> findShellPlugin(String command);

  void addCommand(Pair<ShellPlugin, String> command);

  void executeScript();
}
