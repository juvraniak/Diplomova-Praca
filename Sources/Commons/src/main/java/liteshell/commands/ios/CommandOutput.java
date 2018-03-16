package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xvraniak@stuba.sk
 */

public interface CommandOutput {

    Optional<Stream<String>> getCommandOutput();

    Optional<Stream<String>> getCommandErrorOutput();

    void setCommandOutput(Optional<Stream<String>> commandOutput);

    void setCommandErrorOutput(Optional<Stream<String>> commandErrorOutput);
}
