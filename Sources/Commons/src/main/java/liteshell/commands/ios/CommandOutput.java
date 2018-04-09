package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xvraniak@stuba.sk
 */

public interface CommandOutput {

    Optional<Integer> getReturnCode();

    Optional<Stream<String>> getCommandOutput();

    Optional<Stream<String>> getCommandErrorOutput();

    void setReturnCode(Integer returnCode);

    void setCommandOutput(Stream<String> commandOutput);

    void setCommandErrorOutput(Stream<String> commandErrorOutput);
}
