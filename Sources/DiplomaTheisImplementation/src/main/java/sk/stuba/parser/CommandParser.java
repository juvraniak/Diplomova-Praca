package sk.stuba.parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sk.stuba.scopes.ApplicationScope;
import terminal.common.scopes.Scope;

public class CommandParser implements Parser {

    @Override
    public void parse(String command, Scope scope) {
        if (command.startsWith("./")) {
            parseAndRunScript(command, scope);
        } else {
            ApplicationScope.getRegistrator().getCommand(command.split(" ")[0], "1.0").execute(command, scope);
        }
    }

    private void parseAndRunScript(String command, Scope scope) {
        Path path = Paths.get(command.substring(2, command.length() - 1));
        try (BufferedReader r = Files.newBufferedReader(path, Charset.defaultCharset())) {
            r.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
