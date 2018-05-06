package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.exceptions.CommandIOException;
import liteshell.receivers.ChangeDirectoryReceiver;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ChangeDirectoryCommand implements Command {

  Receiver receiver = new ChangeDirectoryReceiver();

  @Override
  public CommandIO execute(CommandIO commandInput, Optional<Scope> optional) {
    if (commandInput.getCommandInput().isPresent()) {
      return receiver
          .executeCommand(commandInput, parseComand(commandInput.getCommandInput().get()),
              optional);
    } else {
      throw new CommandIOException("Input not found");
    }
  }

  @Override
  public String[] parseComand(Stream<String> stream) {
    return stream.findFirst().get().split("[^\\\\] ");
  }
}
