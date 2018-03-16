package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xvraniak@stuba.sk
 */

public interface CommandInput {

    Optional<Stream<String>> getCommandInput();
}
