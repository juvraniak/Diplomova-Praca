package liteshell.scopes;

import java.util.Optional;
import liteshell.executors.Executor;
import liteshell.plugins.ShellPlugin;

/**
 * @author xvraniak@stuba.sk
 */

public interface Scope {

  ScopeVariables getScopeVariables();

  default Scope getScope() {
        return this;
    }

  String getCurrentWorkingDirectory();

  Optional<ShellPlugin> findShellPlugin(String command);

  void addCommand(String command);

  void executeScript();

  Executor getExecutor();
}
