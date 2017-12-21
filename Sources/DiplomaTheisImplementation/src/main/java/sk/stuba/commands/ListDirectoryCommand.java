package sk.stuba.commands;

import sk.stuba.receivers.Executable;
import sk.stuba.receivers.ListDirectoryReceiver;

public class ListDirectoryCommand implements Command {
    private Executable receiver = new ListDirectoryReceiver();

    @Override
    public void execute(String command) {
        //parse command
        receiver.executeCommand(parseComand(command));
    }

    @Override
    public String[] parseComand(String command) {
        return command.split(" ");
    }


}
