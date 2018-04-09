package liteshell;

import liteshell.executors.Executor;
import liteshell.executors.ExecutorImpl;
import liteshell.plugins.PluginFactory;
import liteshell.scopes.ApplicationScope;
import liteshell.scopes.Scope;

import java.io.IOException;

/**
 * @author xvraniak@stuba.sk
 */

public class Application {

    public static void main(String[] args) {
        PluginFactory pluginFactory = PluginFactory.init();
        Executor executor = new ExecutorImpl();

        Scope application = new ApplicationScope(pluginFactory, executor);

        try {
            ((ApplicationScope) application).run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
