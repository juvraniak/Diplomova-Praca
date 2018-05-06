package liteshell.v1.receivers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;


public class MoveReceiver implements Receiver {

    @Override
    public CommandIO executeCommand(CommandIO commandIO, String[] args, Optional<Scope> scope) {

        Path sourcePath = Paths.get(args[1]);
        Path destPath = Paths.get(args[2]);
        try {
            Files.move(sourcePath, destPath);
            commandIO.setCommandOutput(Optional.of(Stream.of("Files were moved.")));
            commandIO.setReturnCode(0);
        } catch (IOException e) {
            commandIO.setCommandErrorOutput(Optional.of(Stream.of(e.getMessage())));
            commandIO.setReturnCode(-1);
        }finally {
            return commandIO;
        }
    }
}
