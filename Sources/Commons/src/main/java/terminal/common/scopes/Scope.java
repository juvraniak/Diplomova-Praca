package terminal.common.scopes;

import java.util.Map;

/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 19/01/2018.
 */

public interface Scope {

    Scope getScope();

    void setScopeParameters(Map<String, String> parameters);

    ScopeData getScopeData();
}
