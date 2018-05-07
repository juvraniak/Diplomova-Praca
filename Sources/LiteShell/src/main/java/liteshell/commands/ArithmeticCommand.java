package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.receivers.ArithmeticReceiver;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ArithmeticCommand implements Command {

  Receiver receiver = new ArithmeticReceiver();

  @Override
  public CommandIO execute(CommandIO commandIO, Optional<Scope> optional) {
    return receiver
        .executeCommand(commandIO, parseComand(commandIO.getCommandInput().get()), optional);
  }

  @Override
  public String[] parseComand(Stream<String> stream) {
    return stream.findFirst().get().split(" ");
  }
}
