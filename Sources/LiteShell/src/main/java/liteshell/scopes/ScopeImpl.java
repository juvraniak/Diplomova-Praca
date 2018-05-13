package liteshell.scopes;

import liteshell.keywords.Keyword;
import liteshell.utils.ShellClient;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */
@Slf4j
public class ScopeImpl extends AbstractScope {


  public ScopeImpl(String scopeName, ScopeImpl parent) {
    ShellClient shellClient = ShellClient.getInstance();
    this.scopeName = scopeName;
    this.pluginFactory = shellClient.getPluginFactory();
    this.executor = shellClient.getExecutor();
    if (parent == null) {
      this.parent = this;
    } else {
      this.parent = parent;
    }
    this.scopeVariables.getStringMap().put("pwd", System.getProperty("user.home"));
    this.scopeVariables.getInitializedVariables().put("pwd", Keyword.STRING);
    if (scopeName.equals("application")) {
      loadSystemVariables();
    }
  }

  @Override
  public Scope getScope() {
    return this;
  }

  public String assignReturnValue(String line, Keyword key) {
    switch (key) {
      case INT:
        return "int returnValue = " + line.trim().substring("return ".length());
      case DOUBLE:
        return "double returnValue = " + line.trim().substring("return ".length());
      case STRING:
        return "string returnValue = " + line.trim().substring("return ".length());
      case BOOLEAN:
        return "boolean returnValue = " + line.trim().substring("return ".length());

    }
    return line;
  }
  
}
