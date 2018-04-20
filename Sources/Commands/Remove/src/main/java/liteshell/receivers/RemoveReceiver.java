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

public class RemoveReceiver implements Receiver {


    public CommandOutput executeCommand(String[] args, Optional<Scope> scope) {
        CommandOutput commandOutput = new DefaultOutput();
        Path sourcePath = Paths.get(args[1]);
        try {
            Files.delete(sourcePath);
            commandOutput.setCommandOutput(Stream.of("File deleted."));
            commandOutput.setReturnCode(0);
        } catch (IOException e) {
            commandOutput.setCommandErrorOutput(Stream.of(e.getMessage()));
            commandOutput.setReturnCode(-1);
        }finally {
            return commandOutput;
        }
    }
}
