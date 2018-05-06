package liteshell.v1.commands;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.Command;
import liteshell.commands.ios.CommandIO;
import liteshell.exceptions.CommandIOException;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;
import liteshell.v1.receivers.ListDirectoryReceiver;


public class ListDirectoryCommand implements Command {
    private Receiver receiver = new ListDirectoryReceiver();

    @Override
    public CommandIO execute(CommandIO commandInput, Optional<Scope> scope) {
        if (commandInput.getCommandInput().isPresent()) {
            return receiver
                .executeCommand(commandInput, parseComand(commandInput.getCommandInput().get()),
                    scope);
        } else {
            throw new CommandIOException("Input not found");
        }
    }

    @Override
    public String[] parseComand(Stream<String> stream) {
        return stream.findFirst().get().split(" ");
    }


}
