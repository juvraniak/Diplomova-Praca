package sk.stuba.commands;

public interface Command {
    void execute(String command);
    String[] parseComand(String command);
}
