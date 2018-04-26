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
        String pwd = scope.get().getCurrentWorkingDirectory();
        Stream<String> pwdStream = Stream.of(pwd);
        Optional<Stream<String>> optionalOut = Optional.of(pwdStream);
        commandOutput
            .setCommandOutput(optionalOut);
        commandOutput.setReturnCode(0);
      }
    } catch (Exception e) {
      commandOutput.setCommandErrorOutput(Optional.of(Stream.of(e.getMessage())));
      commandOutput.setReturnCode(-1);
    } finally {
      return commandOutput;
    }
  }
}
