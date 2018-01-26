package parsers;

import consumers.CommandConsumer;
import producers.CommandProducer;

public interface CommandParser {
	CommandProducer parseCommand(CommandConsumer input);
}
