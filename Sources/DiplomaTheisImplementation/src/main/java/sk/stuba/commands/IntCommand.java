package sk.stuba.commands;

import sk.stuba.recievers.IntReciever;
import terminal.common.command.Command;
import terminal.common.scopes.Scope;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class IntCommand implements Command {

    private IntReciever intReciever = new IntReciever();

    @Override
    public void execute(String s, Scope scope) {
        intReciever.executeCommand(parseComand(s), scope);
    }

    @Override
    public String[] parseComand(String s) {
        //TODO: check declaration and definition
        return s.split(" ");
    }
}
