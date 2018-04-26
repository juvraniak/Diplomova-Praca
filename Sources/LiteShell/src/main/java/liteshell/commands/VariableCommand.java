package liteshell.commands;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandInput;
import liteshell.commands.ios.CommandOutput;
import liteshell.exceptions.CommandIOException;
import liteshell.receivers.IntReceiver;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class VariableCommand implements Command {

    @Override
    public CommandOutput execute(CommandInput commandInput, Optional<Scope> scope) {
        if (commandInput.getCommandInput().isPresent()) {
            String[] splitedImport = parseComand(commandInput.getCommandInput().get());
            String commandCase = splitedImport[0];
            CommandOutput out = null;
            switch (commandCase) {
                case "int":
                    out = new IntReceiver().executeCommand(splitedImport, scope);
                    break;
                case "double":
//                    out = new ChangePackageReceiver().executeCommand(splitedImport, scope);
                    break;
                case "boolean":
//                    out = new DeletePackageReceiver().executeCommand(splitedImport, scope);
                    break;
                case "string":
//                    out = new DeletePackageReceiver().executeCommand(splitedImport, scope);
                    break;
            }
            return out;
        } else {
            throw new CommandIOException("Input not found");
        }
    }

    @Override
    public String[] parseComand(Stream<String> stream) {
        return removeEmptyStrings(stream.findFirst().get().split(" "));
    }

    private String[] removeEmptyStrings(String[] split) {
        return Arrays.asList(split).stream().filter(s -> !s.isEmpty()).toArray(String[]::new);
    }
}
