package liteshell.receivers;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;

public class ListDirectoryReceiver implements Receiver {

    @Override
    public CommandOutput executeCommand(String[] args, Optional<Scope> scope) {
        CommandOutput commandOutput = new DefaultOutput();

        File curDir = new File(args[1]);
        File[] filesList = curDir.listFiles();
        Stream<String> out = Arrays.asList(filesList).stream().map(f -> f.getName()).sorted();
        commandOutput.setCommandErrorOutput(Optional.empty());
        commandOutput.setCommandOutput(Optional.of(out));
        return commandOutput;
    }
}
