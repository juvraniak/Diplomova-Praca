package consumers;

import scopes.Scope;

public interface CommandConsumer {
	String getInputForCommand();
	Scope getScopeOfCommand();
}
