package sk.stuba.executor;

import liteshell.scopes.Scope;

public abstract class AbstractExecutor implements Scope{
	 abstract CommandOutput execute(String command, Scope scope);
}
