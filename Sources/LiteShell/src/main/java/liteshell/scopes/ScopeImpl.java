package liteshell.scopes;

import liteshell.keywords.Keyword;
import liteshell.utils.ShellClient;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */
@Slf4j
public class ScopeImpl extends AbstractScope {


  public ScopeImpl(String scopeName, Scope parent) {
    ShellClient shellClient = ShellClient.getInstance();
    this.scopeName = scopeName;
    this.pluginFactory = shellClient.getPluginFactory();
    this.executor = shellClient.getExecutor();
    this.parent = parent;
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
}
