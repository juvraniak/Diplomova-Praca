package liteshell.v1.receivers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;

public class RemoveReceiver implements Receiver {


    public CommandIO executeCommand(CommandIO commandIO, String[] args, Optional<Scope> scope) {
        Path sourcePath = Paths.get(args[1]);
        try {
            Files.delete(sourcePath);
            commandIO.setCommandOutput(CommandIO.prepareIO("File deleted."));
            commandIO.setReturnCode(0);
        } catch (IOException e) {
            commandIO.setCommandErrorOutput(CommandIO.prepareIO(e.getMessage()));
            commandIO.setReturnCode(-1);
        }finally {
            return commandIO;
        }
    }
}
