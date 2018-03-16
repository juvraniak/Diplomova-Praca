package sk.stuba.parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sk.stuba.dependency.DependencyResolver;
import sk.stuba.plugins.JarLoader;
import sk.stuba.plugins.PluginFactory;
import terminal.common.scopes.Scope;

public class ParserImpl implements Parser {

    @Override
    public void parse(String command, Scope scope) {
        if (command.startsWith("./")) {
            parseAndRunScript(command, scope);
        } else {
            PluginFactory.getCommand(command.split(" ")[0]).execute(command, scope);
        }
    }

    private void parseAndRunScript(String command, Scope scope) {
    	//added to generate class diagram
    	DependencyResolver dr = new DependencyResolver();
    	//in case there are some unknown dependencies we need to load them so JarLoader needed here as well.
    	JarLoader.loadJar(Paths.get("", ""));
        Path path = Paths.get(command.substring(2, command.length() - 1));
        try (BufferedReader r = Files.newBufferedReader(path, Charset.defaultCharset())) {
            r.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
