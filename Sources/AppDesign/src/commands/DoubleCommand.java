package commands;

import java.util.Optional;
import java.io.IOException;

import consumers.CommandConsumer;
import parsers.CommandParser;
import producers.CommandProducer;
import receivers.DoubleReceiver;
import receivers.Receiver;

public class DoubleCommand implements CommandParser, Command{
	
	Receiver reciever = new DoubleReceiver();

	@Override
	public Optional<CommandProducer> executeCommand(CommandConsumer commandInput) {
		 int b;
		return Optional.empty();
	}

	@Override
	public CommandProducer parseCommand(CommandConsumer input) {
		// TODO Auto-generated method stub
		return null;
	}
	final public static void main(String... args) {
		System.out.println();
	}
}
