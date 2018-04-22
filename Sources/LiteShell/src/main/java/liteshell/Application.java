package liteshell;

import liteshell.scopes.ApplicationScope;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {

    public static void main(String[] args) {
        Client client = Client.getInstance();

        Runnable application = new ApplicationScope(client);

        application.run();

    }
}
