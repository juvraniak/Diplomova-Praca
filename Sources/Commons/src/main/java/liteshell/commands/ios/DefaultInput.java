package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xvraniak@stuba.sk
 */

@AllArgsConstructor
public class DefaultInput implements CommandInput {

    private final Stream<String> input;

    @Override
    public Optional<Stream<String>> getCommandInput() {
        return Optional.of(input);
    }
}
