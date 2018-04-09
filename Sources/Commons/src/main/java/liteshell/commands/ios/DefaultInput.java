package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xvraniak@stuba.sk
 */


public class DefaultInput implements CommandInput {

    private final Stream<String> input;

    private DefaultInput(Stream<String> input){
        this.input = input;
    }

    @Override
    public Optional<Stream<String>> getCommandInput() {
        return Optional.of(input);
    }

    public static DefaultInput of(Stream<String> input){
        return new DefaultInput(input);
    }
}
