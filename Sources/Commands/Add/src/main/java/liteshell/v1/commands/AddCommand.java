package liteshell.v1.commands;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.Command;
import liteshell.commands.ios.CommandInput;
import liteshell.commands.ios.CommandOutput;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;
import liteshell.v1.receivers.AddReceiver;

/**
 * @author xvraniak@stuba.sk
 */

public class AddCommand implements Command {

  Receiver receiver = new AddReceiver();

  @Override
  public CommandOutput execute(CommandInput commandInput, Optional<Scope> optional) {
    return receiver.executeCommand(parseComand(commandInput.getCommandInput().get()), optional);
  }

  @Override
  public String[] parseComand(Stream<String> stream) {
//    add()
    return new String[]{stream.findFirst().get()};
  }
}
