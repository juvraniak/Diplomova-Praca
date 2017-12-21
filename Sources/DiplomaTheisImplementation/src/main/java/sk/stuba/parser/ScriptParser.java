package sk.stuba.parser;

import java.util.Arrays;
import java.util.List;

import sk.stuba.dependency.DependencyResolver;

public class ScriptParser implements Parser {

    private final DependencyResolver resolver = new DependencyResolver();

    @Override
    public void parse(String command) {
        //look for imports and resolve missing dependencies
        resolver.getDependencies(getImports());
    }

    private List<String> getImports() {
        return Arrays.asList(" ");
    }
}
