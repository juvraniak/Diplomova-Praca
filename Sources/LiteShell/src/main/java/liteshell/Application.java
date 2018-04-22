package liteshell;

import liteshell.scopes.ApplicationScope;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {

    public static void main(String[] args) {
        ShellClient shellClient = ShellClient.getInstance();

        Runnable application = new ApplicationScope(shellClient);

        application.run();

    }
}
