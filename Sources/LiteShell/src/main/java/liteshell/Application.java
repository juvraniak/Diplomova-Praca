package liteshell;

import liteshell.scopes.ScopeImpl;
import liteshell.utils.ShellClient;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {
  public static void main(String[] args) {
    ShellClient shellClient = ShellClient.getInstance();
    Runnable application = new ScopeImpl("application", shellClient, null);
    application.run();
  }
}
