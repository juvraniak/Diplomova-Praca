package liteshell.scopes;

import java.util.List;
import liteshell.parser.Parser;

/**
 * @author xvraniak@stuba.sk
 */

public class ForScope extends ScopeImpl implements Parser {


  public ForScope(String scopeName, Scope parent, List<String> content, int index) {
    super(scopeName, parent);
  }

}
