package sk.stuba.parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.Iterator;

import sk.stuba.dependency.DependencyResolver;
import sk.stuba.registration.JarLoader;
import sk.stuba.scopes.ScopeImpl;
import terminal.common.scopes.Scope;

public class ParserImpl implements Parser {

    @Override
    public void parse(String command, Scope scope) {
        if (command.startsWith("./")) {
            parseAndRunScript(command, scope);
        } else {
            ScopeImpl.getRegistrator().getCommand(command.split(" ")[0], "1.0").execute(command, scope);
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
