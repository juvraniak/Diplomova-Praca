import commandcommons.CommandInputImpl;
import commands.Command;
import commands.DoubleCommand;
import scopes.AppScope;

public class Main {
    public static void main(String[] args) {
        Command command = new DoubleCommand();
        AppScope appScope = new AppScope();

        command.fireUpCommand(new CommandInputImpl("copy path1 path2"), appScope);

    }
}
