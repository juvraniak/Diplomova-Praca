package plugins;

import java.util.Optional;

import commands.Command;

public interface AppPlugin {
	Optional<Command> getPluginCommand();
}
