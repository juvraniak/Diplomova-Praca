package liteshell.scopes;

import java.util.List;

/**
 * @author xvraniak@stuba.sk
 */

public class ForScope extends ScopeImpl {


  public ForScope(String scopeName, Scope parent, List<String> content, int index) {
    super(scopeName, parent);
  }

}
