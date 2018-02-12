package sk.jv.commands;

import sk.jv.receivers.CopyReceiver;
import terminal.common.command.Command;
import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

public class CopyCommand implements Command {

    private Executable receiver = new CopyReceiver();

    @Override
    public void execute(String s, Scope scope) {
        receiver.executeCommand(parseComand(s), scope);
    }

    @Override
    public String[] parseComand(String command) {
        return command.split(" ");
    }
}
