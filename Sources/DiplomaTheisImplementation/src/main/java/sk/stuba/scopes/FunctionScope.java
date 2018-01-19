package sk.stuba.scopes;

import java.util.Map;

import terminal.common.scopes.Scope;
import terminal.common.scopes.ScopeData;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class FunctionScope implements Scope {


    @Override
    public Scope getScope() {
        return null;
    }

    @Override
    public void setScopeParameters(Map<String, String> map) {

    }

    @Override
    public ScopeData getScopeData() {
        return null;
    }
}
