package liteshell.commands;

import liteshell.commands.ios.CommandInput;
import liteshell.commands.ios.CommandOutput;
import liteshell.exceptions.CommandIOException;
import liteshell.receivers.GrepReceiver;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xvraniak@stuba.sk
 */

public class GrepCommand implements Command {
    Receiver receiver = new GrepReceiver();

    @Override
    public CommandOutput execute(CommandInput commandInput, Optional<Scope> optional) {
        if(commandInput.getCommandInput().isPresent()){
            return receiver.executeCommand(parseComand(commandInput.getCommandInput().get()), optional);
        }else{
            throw new CommandIOException("Input not found");
        }
    }

    @Override
    public String[] parseComand(Stream<String> stream) {
        Object[] objects = stream.toArray();
        StringBuilder sb = new StringBuilder(10);
        for(Object o : objects){
            sb.append(o).append(" ");
        }
        return sb.toString().split(" ");
    }
}
