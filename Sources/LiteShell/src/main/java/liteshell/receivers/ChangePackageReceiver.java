package liteshell.receivers;

import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ChangePackageReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings, Optional<Scope> optional) {
    return null;
  }
}
