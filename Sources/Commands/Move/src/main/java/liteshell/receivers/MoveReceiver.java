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
            commandOutput.setCommandOutput(Optional.of(Stream.of("Files were moved.")));
            commandOutput.setReturnCode(0);
        } catch (IOException e) {
            commandOutput.setCommandErrorOutput(Optional.of(Stream.of(e.getMessage())));
            commandOutput.setReturnCode(-1);
        }finally {
            return commandOutput;
        }
    }
}
