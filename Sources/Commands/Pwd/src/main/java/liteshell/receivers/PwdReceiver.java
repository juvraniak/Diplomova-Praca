package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;

public class PwdReceiver implements Receiver {


  public CommandOutput executeCommand(String[] args, Optional<Scope> scope) {
    CommandOutput commandOutput = new DefaultOutput();

    try {
      if (scope.isPresent()) {
        commandOutput.setCommandOutput(Stream.of(scope.get().getCurrentWorkingDirectory()));
        commandOutput.setReturnCode(0);
      }
    } catch (Exception e) {
      commandOutput.setCommandErrorOutput(Stream.of(e.getMessage()));
      commandOutput.setReturnCode(-1);
    } finally {
      return commandOutput;
    }
  }
}
