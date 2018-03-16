package liteshell.scopes;

import java.util.Stack;

import liteshell.executors.Executor;
import liteshell.plugins.PluginFactory;


/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class ScopeImpl implements Scope {

    protected static String scopeName;
    protected static Stack callStack;
    protected static PluginFactory pluginFactory;
    protected static Executor executor;
    private final ScopeData scopeData = new ScopeData();

    public ScopeImpl(String scopeName, PluginFactory pluginFactory, Executor executor) {
        this.scopeName = scopeName;
        this.callStack = new Stack();
        this.pluginFactory = pluginFactory;
        this.executor = executor;
    }

    @Override
    public ScopeData getScopeData() {
        return scopeData;
    }
}
