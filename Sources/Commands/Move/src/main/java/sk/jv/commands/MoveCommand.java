package sk.jv.commands;

import sk.jv.receivers.MoveReceiver;
import terminal.common.command.Command;
import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

public class MoveCommand implements Command {

    private Executable receiver = new MoveReceiver();

    @Override
    public void execute(String command, Scope scope) {
        receiver.executeCommand(parseComand(command), scope);
    }

    @Override
    public String[] parseComand(String command) {
        return command.split(" ");
    }
}
