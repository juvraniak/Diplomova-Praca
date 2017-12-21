package sk.stuba.commands;

import sk.stuba.receivers.Executable;
import sk.stuba.receivers.MoveReceiver;

public class MoveCommand implements Command {

    private Executable receiver = new MoveReceiver();

    @Override
    public void execute(String command) {
        receiver.executeCommand(parseComand(command));
    }

    @Override
    public String[] parseComand(String command) {
        return command.split(" ");
    }
}
