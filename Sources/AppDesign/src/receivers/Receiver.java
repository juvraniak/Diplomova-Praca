package receivers;

import java.util.Optional;

import consumers.CommandConsumer;
import producers.CommandProducer;

public interface Receiver {
	Optional<CommandProducer> executeCommand(CommandConsumer commandInput);
}
