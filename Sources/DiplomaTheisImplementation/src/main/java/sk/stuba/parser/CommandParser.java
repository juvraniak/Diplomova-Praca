package sk.stuba.parser;


import sk.stuba.main.ApplicationLoader;

public class CommandParser implements Parser {

    @Override
    public void parse(String command) {
        ApplicationLoader.getRegistrator().getCommand(command.split(" ")[0]).execute(command);
    }
}
