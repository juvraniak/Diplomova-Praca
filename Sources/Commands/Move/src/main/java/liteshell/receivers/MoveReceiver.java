package liteshell.receivers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;


import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;


public class MoveReceiver implements Receiver {

    @Override
    public CommandOutput executeCommand(String[] args, Optional<Scope> scope) {
        CommandOutput commandOutput = new DefaultOutput();
        Path sourcePath = Paths.get(args[1]);
        Path destPath = Paths.get(args[2]);
        try {
            Files.move(sourcePath, destPath);
            commandOutput.setCommandOutput(Stream.of("Files were moved."));
        } catch (IOException e) {
            commandOutput.setCommandErrorOutput(Stream.of(e.getMessage()));
        }finally {
            return commandOutput;
        }
    }
}
