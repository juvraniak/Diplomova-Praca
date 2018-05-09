package liteshell.scopes;

import java.util.List;
import liteshell.parser.Parser;

/**
 * @author xvraniak@stuba.sk
 */

public class IfScope extends ScopeImpl implements Parser {

  public IfScope(String scopeName, Scope parent, List<String> content, int index) {
    super(scopeName, parent);
  }
  //budem si vytvarat premenne na urcovanie booleanu v ife

}
