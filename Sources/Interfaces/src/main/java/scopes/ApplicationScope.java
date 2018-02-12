package scopes;

import java.util.Map;

public interface ApplicationScope {
    void run();

    ApplicationScope getScope();

    void setScopeParameters(Map<String, String> var1);

    ScopeData getScopeData();
}
