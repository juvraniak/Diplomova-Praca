package liteshell.scopes;

/**
 * @author xvraniak@stuba.sk
 */

public interface Scope {

    ScopeData getScopeData();

    default Scope getScope() {
        return this;
    }

    String getCurrentWorkingDirectory();
}
