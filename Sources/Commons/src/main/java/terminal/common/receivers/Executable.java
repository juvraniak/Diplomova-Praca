package terminal.common.receivers;

import terminal.common.scopes.Scope;

public interface Executable {
    void executeCommand(String[] args, Scope scope);
}
