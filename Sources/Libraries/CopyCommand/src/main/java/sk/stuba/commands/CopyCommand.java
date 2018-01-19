package sk.stuba.commands;

import sk.stuba.receivers.CopyReceiver;
import sk.stuba.receivers.Executable;

public class CopyCommand implements Command {

    private Executable receiver = new CopyReceiver();

    @Override
    public void execute(String command) {
        receiver.executeCommand(parseComand(command));
    }

    @Override
    public String[] parseComand(String command) {
        return command.split(" ");
    }
}
