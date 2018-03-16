package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;

import liteshell.commands.ios.CommandInput;
import liteshell.commands.ios.CommandOutput;
import liteshell.exceptions.CommandIOException;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;
import liteshell.receivers.MoveReceiver;


public class MoveCommand implements Command {

    private Receiver receiver = new MoveReceiver();

    @Override
    public CommandOutput execute(CommandInput commandInput, Optional<Scope> scope) {
        if (commandInput.getCommandInput().isPresent()) {
            return receiver.executeCommand(parseComand(commandInput.getCommandInput().get()), scope);
        } else {
            throw new CommandIOException("Input not found");
        }
    }

    @Override
    public String[] parseComand(Stream<String> stream) {
        return stream.findFirst().get().split(" ");
    }
}