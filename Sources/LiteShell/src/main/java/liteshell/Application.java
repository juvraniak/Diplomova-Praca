package liteshell;

import liteshell.scopes.ScopeImpl;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {
  public static void main(String[] args) {
    Runnable application = new ScopeImpl("application", null);
    application.run();
  }
}
