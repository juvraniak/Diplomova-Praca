package receivers;

import java.util.Optional;

import consumers.CommandConsumer;
import producers.CommandProducer;

public class DoubleReceiver implements Receiver{

	@Override
	public Optional<CommandProducer> executeCommand(CommandConsumer commandInput) {
		return Optional.empty();
	}

}
