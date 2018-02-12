package sk.stuba.scopes;

import java.util.Optional;

import terminal.common.scopes.Scope;

public class ScopeFactory {
	
	private static final Scope applicationScope = new ScopeImpl();
	
	public static Optional<Scope> createScope(String type) {
		if(type.equals("application")) {
			return Optional.of(applicationScope);
		} else if(type.equals("script")) {
			return Optional.of(new ScopeImpl());
		} else if(type.equals("function")) {
			return Optional.of(new ScopeImpl());
		}
		return Optional.empty();
	}
}
