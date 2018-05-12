package liteshell;

import java.util.Arrays;
import java.util.List;
import liteshell.scopes.ScopeImpl;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {
  public static void main(String[] args) {

    Runnable application = new ScopeImpl("application", null);
    if (args.length > 0) {
      List<String> inptutCommands = Arrays.asList(args);
      ((ScopeImpl) application).setInptutCommands(inptutCommands);
      ((ScopeImpl) application).runInputCommands();
      return;
    }
    application.run();
  }
}
