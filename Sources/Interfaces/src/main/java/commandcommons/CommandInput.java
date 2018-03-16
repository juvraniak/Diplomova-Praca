package commandcommons;


import java.util.stream.Stream;

public interface CommandInput {
    Stream<String> getCommandInput();
}
