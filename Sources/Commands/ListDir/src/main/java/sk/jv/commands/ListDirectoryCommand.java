package sk.jv.commands;

import sk.jv.receivers.ListDirectoryReceiver;
import terminal.common.command.Command;
import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

public class ListDirectoryCommand implements Command {
    private Executable receiver = new ListDirectoryReceiver();

    @Override
    public void execute(String command, Scope scope) {
        //parse command
        receiver.executeCommand(parseComand(command), scope);
    }

    @Override
    public String[] parseComand(String command) {
        return command.split(" ");
    }


}
