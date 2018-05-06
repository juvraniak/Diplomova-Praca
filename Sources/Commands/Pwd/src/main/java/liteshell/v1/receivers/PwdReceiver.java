package liteshell.v1.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;

public class PwdReceiver implements Receiver {


  public CommandIO executeCommand(CommandIO commandIO, String[] args, Optional<Scope> scope) {


    try {
      if (scope.isPresent()) {
        String pwd = scope.get().getCurrentWorkingDirectory();
        Stream<String> pwdStream = Stream.of(pwd);
        Optional<Stream<String>> optionalOut = Optional.of(pwdStream);
        commandIO
            .setCommandOutput(optionalOut);
        commandIO.setReturnCode(0);
      }
    } catch (Exception e) {
      commandIO.setCommandErrorOutput(Optional.of(Stream.of(e.getMessage())));
      commandIO.setReturnCode(-1);
    } finally {
      return commandIO;
    }
  }
}
