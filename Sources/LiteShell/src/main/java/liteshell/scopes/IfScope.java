package liteshell.scopes;

import java.util.List;

/**
 * @author xvraniak@stuba.sk
 */

public class IfScope extends ScopeImpl {

  public IfScope(String scopeName, Scope parent, List<String> content, int index) {
    super(scopeName, parent);
  }
  //budem si vytvarat premenne na urcovanie booleanu v ife

}
