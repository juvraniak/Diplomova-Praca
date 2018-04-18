package liteshell;

import java.io.IOException;
import liteshell.scopes.ApplicationScope;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {

    public static void main(String[] args) {
        Client client = Client.getInstance();

        Scope application = new ApplicationScope(client);

        try {
            ((ApplicationScope) application).run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
