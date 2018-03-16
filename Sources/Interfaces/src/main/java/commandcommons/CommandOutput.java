package commandcommons;

import java.util.Optional;
import java.util.stream.Stream;

public interface CommandOutput {
    Optional<Stream<String>> getCommandOutput();
    Optional<Stream<String>> getCommandErrorOutput();
}
