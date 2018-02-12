package sk.stuba.main;

import java.io.IOException;
import java.util.Optional;

import sk.stuba.scopes.ScopeFactory;
import sk.stuba.scopes.ScopeImpl;
import terminal.common.scopes.Scope;

public class ApplicationLoader {


    public static void main(String[] args) throws IOException {
        Optional<Scope> application = ScopeFactory.createScope("application");
        if(application.isPresent()) {
        	ScopeImpl appScope = (ScopeImpl) application.get();
        	appScope.run();
        }
    }



}
