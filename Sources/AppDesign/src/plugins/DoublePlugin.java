package plugins;

import java.util.Optional;

import commands.Command;
import commands.DoubleCommand;

public class DoublePlugin implements AppPlugin{

	@Override
	public Optional<Command> getPluginCommand() {
		Command command = new DoubleCommand();
		return Optional.of(command);
	}

}
