package liteshell.executors;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.util.Pair;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
import liteshell.exceptions.MethodMissingEception;
import liteshell.exceptions.UnknownCommandException;
import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xvraniak@stuba.sk
 */
@Slf4j
public class ExecutorImpl implements Executor {

  /**
   * use in not piped command
   *
   * @param command command string that will be taken to execute method
   * @param scope scope where command will be executed
   */
  @Override
  public void execute(String command, Scope scope) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command();

    if (command.startsWith("./") && command.endsWith(".sh")) {
      //TODO: handle execution of shell script

    } else if (command.startsWith("./") && command.endsWith(".bat")) {
      //TODO: handle execution of windows script
    } else if (command.startsWith("./") && command.endsWith(".ls")) {
      String path = validatePath(command.substring(2), scope);
      try {
        if (path == null) {
          throw new NullPointerException("Enter correct path!");
        }
        ScriptParser parser = new ScriptParser(path);
        parser.parse(path);
      } catch (MethodMissingEception e) {
        log.error("Problem during parsing script :\n{}", e.getMessage());
      }
    } else {
      executeSingleCommand(command, scope);
    }
  }

  private void executeSingleCommand(String command, Scope scope) {
    String requestedCommand = command.split(" ")[0];
    Optional<ShellPlugin> plugin = scope.findShellPlugin(requestedCommand);
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

  private String validatePath(String filePath, Scope scope) {
    String pathWithPwd = scope.getCurrentWorkingDirectory() + File.separator + filePath;
    File withoutPwd = new File(filePath);
    File withPwd = new File(pathWithPwd);
    if (withPwd.exists() && !withPwd.isDirectory()) {
      return pathWithPwd;
    } else if (withoutPwd.exists() && !withoutPwd.isDirectory()) {
      return filePath;
    }
    return null;
  }

  /**
   * @param scope scope where list of commands should be executed
   * @param list list of pairs ShellPlugin and command that should be run on ShellPlugin command
   */
//  @Override
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
        if (out.getCommandOutput().isPresent()) {
          out.getCommandOutput().get().forEach(System.out::println);
        }
      }
    } else {
      onError(out);
    }
  }
}
