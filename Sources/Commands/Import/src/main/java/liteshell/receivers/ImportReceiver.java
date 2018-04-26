package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;

public class ImportReceiver implements Receiver {


  public CommandOutput executeCommand(String[] args, Optional<Scope> scope) {
    CommandOutput commandOutput = new DefaultOutput();

    try {
      commandOutput.setCommandOutput(Optional.of(Stream.of("Dummy importer.")));
      commandOutput.setReturnCode(0);
    } catch (Exception e) {
      commandOutput.setCommandErrorOutput(Optional.of(Stream.of(e.getMessage())));
      commandOutput.setReturnCode(-1);
    } finally {
      return commandOutput;
    }
  }
}
