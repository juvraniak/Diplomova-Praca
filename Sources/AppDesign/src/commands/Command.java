package commands;

import java.util.Optional;

import consumers.CommandConsumer;
import producers.CommandProducer;

public interface Command {
	Optional<CommandProducer> executeCommand(CommandConsumer commandInput);
	
}
