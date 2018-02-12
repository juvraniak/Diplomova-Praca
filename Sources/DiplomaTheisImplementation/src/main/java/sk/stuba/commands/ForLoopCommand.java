package sk.stuba.commands;

import sk.stuba.recievers.ForLoopReciever;
import terminal.common.command.Command;
import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class ForLoopCommand implements Command {

    private Executable forLoopReciever = new ForLoopReciever();

    @Override
    public void execute(String s, Scope scope) {
        forLoopReciever.executeCommand(parseComand(s), scope);
    }

    @Override
    public String[] parseComand(String s) {
        //TODO: parse here or do only check whether for loop is created correctly?
        return new String[0];
    }
}
