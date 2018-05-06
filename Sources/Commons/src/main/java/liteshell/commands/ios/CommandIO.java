package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xvraniak@stuba.sk
 */

public interface CommandIO {

  Integer getReturnCode();

  Optional<Stream<String>> getCommandOutput();

  Optional<Stream<String>> getCommandErrorOutput();

  Optional<Stream<String>> getCommandInput();

  void setReturnCode(Integer returnCode);

  void setCommandOutput(Optional<Stream<String>> commandOutput);

  void setCommandErrorOutput(Optional<Stream<String>> commandErrorOutput);

  static Optional<Stream<String>> prepareIO(String string) {
    return Optional.of(Stream.of(string));
  }
}
