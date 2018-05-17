package liteshell.receivers;

import java.util.Optional;

import liteshell.commands.ios.CommandIO;
import liteshell.scopes.AbstractScope;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ChangePackageReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings, Optional<Scope> optional) {
    AbstractScope scope = (AbstractScope) optional.get();
    scope.getPluginFactory().changePlugin(strings[2] + " " + strings[3]);
    return commandIO;
  }
}
