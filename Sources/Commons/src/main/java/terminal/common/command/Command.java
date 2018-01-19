package terminal.common.command;

import terminal.common.scopes.Scope;

public interface Command {
    void execute(String command, Scope scope);
    String[] parseComand(String command);
}
