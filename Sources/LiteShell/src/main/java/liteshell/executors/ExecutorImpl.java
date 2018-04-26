package liteshell.executors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.util.Pair;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
import liteshell.exceptions.UnknownCommandException;
import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ExecutorImpl implements Executor {

  /**
   * use in not piped command
   *
   * @param plugin plugin carrying executive command
   * @param command command string that will be taken to execute method
   * @param scope scope where command will be executed
   */
  @Override
  public void execute(Optional<ShellPlugin> plugin, String command, Scope scope) {
    ProcessBuilder processBuilder = new ProcessBuilder();

    if (command.startsWith("./")) {
      //handle script - maybe i will handle shell scripts by this
      processBuilder.command();
    } else {
      String requestedCommand = command.split(" ")[0];
      if (plugin.isPresent()) {
        CommandOutput output = plugin.get().getCommand()
            .execute(DefaultInput.of(Stream.of(command)), Optional.of(scope));
        if (output.getCommandOutput().isPresent()) {
          output.getCommandOutput().get().forEach(System.out::println);
        }
      } else {
        throw new UnknownCommandException(requestedCommand);
      }
    }
  }

  /**
   * @param scope scope where list of commands should be executed
   * @param list list of pairs ShellPlugin and command that should be run on ShellPlugin command
   */
  @Override
  public void execute(Scope scope, List<Pair<Optional<ShellPlugin>, String>> list) {
    validatePluginList(list);

    ExecutorListener temp = null;
    for (int i = list.size() - 1; i > -1; i--) {
      Pair<Optional<ShellPlugin>, String> item = list.get(i);
      PipeExecutor executor = PipeExecutor
          .of(Stream.of(item.getValue()), item.getKey().get(), scope, temp);
      temp = executor;
    }
    temp.run();
  }

  private void validatePluginList(List<Pair<Optional<ShellPlugin>, String>> list) {
    list.forEach(pair -> {
      if (!pair.getKey().isPresent() || pair.getValue().length() < 1) {
        throw new RuntimeException();
      }
    });
  }
}

interface ExecutorListener extends Runnable {

  void finishedExecution(CommandOutput output);

  void onError(CommandOutput output);
}

class PipeExecutor implements ExecutorListener {

  private ShellPlugin plugin;
  private Stream<String> command;
  private Scope scope;
  private ExecutorListener listener;

  private PipeExecutor(Stream<String> command, ShellPlugin plugin, Scope scope,
      ExecutorListener listener) {
    this.command = command;
    this.plugin = plugin;
    this.scope = scope;
    this.listener = listener;
  }

  public static PipeExecutor of(Stream<String> command, ShellPlugin plugin, Scope scope,
      ExecutorListener listener) {
    return new PipeExecutor(command, plugin, scope, listener);
  }


  @Override
  public void finishedExecution(CommandOutput output) {
    command = Stream.concat(command, output.getCommandOutput().get());
    run();
  }

  @Override
  public void onError(CommandOutput output) {
    throw new RuntimeException(
        "Return code : " + output.getReturnCode() + "\n" + output.getCommandErrorOutput().get()
            .findFirst());
  }

  @Override
  public void run() {
    CommandOutput out = plugin.getCommand().execute(DefaultInput.of(command), Optional.of(scope));
    if (out.getReturnCode() == 0) {
      if (listener != null) {
        listener.finishedExecution(out);
      } else {
        if (plugin.shouldPrint()) {
          out.getCommandOutput().get().forEach(System.out::println);
        }
      }
    } else {
      onError(out);
    }
  }
}
