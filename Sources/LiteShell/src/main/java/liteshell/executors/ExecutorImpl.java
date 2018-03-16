package liteshell.executors;

import java.util.Optional;
import java.util.stream.Stream;

import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
import liteshell.exceptions.UnknownCommandException;
import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ExecutorImpl implements Executor {

    @Override
    public void execute(Optional<ShellPlugin> plugin, String command, Scope scope) {

        if (command.startsWith("./")) {
            //handle script - maybe i will handle shell scripts by this
        } else {
            String requestedCommand = command.split(" ")[0];
            if (plugin.isPresent()) {
                CommandOutput output = plugin.get().getCommand().execute(new DefaultInput(Stream.of(command)), Optional.of(scope));
                if (output.getCommandOutput().isPresent()) {
                    output.getCommandOutput().get().forEach(System.out::println);
                }
            } else {
                throw new UnknownCommandException(requestedCommand);
            }
        }
    }
}
