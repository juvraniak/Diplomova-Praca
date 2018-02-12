package commands;

import commandcommons.CommandInput;
import commandcommons.CommandOutput;
import parsers.CommandParser;
import parsers.DoubleParser;
import receivers.DoubleReceiver;
import receivers.Receiver;
import scopes.ApplicationScope;

public class DoubleCommand implements Command {
    Receiver receiver = new DoubleReceiver();
    CommandParser parser = new DoubleParser();

    @Override
    public CommandOutput fireUpCommand(CommandInput input, ApplicationScope scope) {
        return receiver.receiveAndExecute(parser.parse(input.getCommandInput()), scope);
    }


}
