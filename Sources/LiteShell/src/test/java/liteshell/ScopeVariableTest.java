package liteshell;

import liteshell.scopes.ScopeImpl;
import liteshell.scopes.ScopeVariables;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author xvraniak@stuba.sk
 */

public class ScopeVariableTest {

  @Test
  public void testCopy() throws CloneNotSupportedException {
    ScopeImpl scope = new ScopeImpl("application", null);
    ScopeVariables copiedVar = scope.getScopeVariables().clone();
    Assert.assertNotEquals(scope.getScopeVariables(), copiedVar);
  }
}
