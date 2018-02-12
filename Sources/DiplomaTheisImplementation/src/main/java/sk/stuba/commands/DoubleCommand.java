package sk.stuba.commands;

import sk.stuba.recievers.DoubleReciever;
import terminal.common.command.Command;
import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class DoubleCommand implements Command {

    private Executable intReciever = new DoubleReciever();

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
