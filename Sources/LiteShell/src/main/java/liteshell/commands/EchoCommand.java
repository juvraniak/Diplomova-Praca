package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.receivers.EchoReceiver;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class EchoCommand implements Command {

  Receiver receiver = new EchoReceiver();

  @Override
  public CommandIO execute(CommandIO commandIO, Optional<Scope> optional) {
    return receiver
        .executeCommand(commandIO, parseComand(commandIO.getCommandInput().get()), optional);
  }

  @Override
  public String[] parseComand(Stream<String> stream) {
    String input = stream.reduce(String::concat).get();
    input = input.substring("echo (".length(), input.length() - 1);

    return new String[]{input};
  }
}
