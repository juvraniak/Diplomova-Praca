package liteshell.executors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.util.Pair;
import liteshell.commands.VariableCommand;
import liteshell.commands.ios.CommandIO;
import liteshell.commands.ios.DefaultCommadIO;
import liteshell.exceptions.MethodMissingEception;
import liteshell.exceptions.UnknownCommandException;
import liteshell.plugins.ShellPlugin;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xvraniak@stuba.sk
 */
@Slf4j
public class ExecutorImpl implements Executor {

  boolean writeToStdOut;
  boolean writeToStdErr;

  /**
   * use in not piped command
   *
   * @param command command string that will be taken to execute method
   * @param scope scope where command will be executed
   */
  @Override
  public CommandIO execute(String command, Scope scope) {
    CommandIO out = DefaultCommadIO.of(CommandIO.prepareIO(command));

    boolean isStdOut = command.matches("stdout>");
    boolean isStdErr = command.matches("stderr>");
    String fileToSave = "";

    if (isStdOut) {
      fileToSave = command.split("stdout>")[1];
      command = command.split("stdout>")[0];
    } else if (isStdErr) {
      fileToSave = command.split("stderr>")[1];
      command = command.split("stderr>")[0];
    }

    if (command.startsWith("sh ")) {
      executeCommand(command.substring(3));
    } else if (command.startsWith("win ") || command.startsWith("ext ")) {
      executeCommand(command.substring(4));
    } else if (command.startsWith("./")) {
      String path = validatePath(command.substring(1), scope);
      try {
        if (path == null) {
          throw new NullPointerException("Enter correct path!");
        }
        RootParser parser = new RootParser(path);
        parser.parse((ScopeImpl) scope);
      } catch (MethodMissingEception | CloneNotSupportedException e) {
        log.error("Problem during parsing script :\n{}", e.getMessage());
      }
    } else if (command.startsWith("${")) {
      out = new VariableCommand()
          .execute(DefaultCommadIO.of(CommandIO.prepareIO(command)), Optional.of(scope));
    } else if (command.startsWith("$(")) {
      if (command.endsWith(";")) {
        command = command.substring(0, command.length() - 1);
      }
      command = command.substring(command.indexOf("(") + 1, command.length() - 1);
      if (command.split("\\s{1}[|]\\s{1}").length > 1) {
        out = executePipe(scope, command);
      } else {
        out = executeSingleCommand(command, scope);
      }
    } else {

    }
    if (isStdErr || isStdOut) {
      File f = new File(fileToSave);
      try (FileOutputStream fos = new FileOutputStream(f)) {
        if (out.getCommandErrorOutput().isPresent()) {
          Optional<String> toWrite = out.getCommandOutput().get().reduce(String::concat);
          if (toWrite.isPresent()) {
            fos.write(toWrite.get().getBytes());
          }
        } else if (out.getCommandErrorOutput().isPresent()) {
          Optional<String> toWrite = out.getCommandErrorOutput().get().reduce(String::concat);
          if (toWrite.isPresent()) {
            fos.write(toWrite.get().getBytes());
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        return  DefaultCommadIO.of(CommandIO.prepareIO(command));
      }
    } else {
      return out;
    }
  }

  private CommandIO executeCommand(String command) {
    //FIXME ani za nic to nechce ukladat do suboru.
//    String[] commandSplit = command.split(" > ");
//    File f = new File(commandSplit[1]);
//    try {
//      f.createNewFile();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    ProcessBuilder builder = new ProcessBuilder("sh", command);
//    builder.redirectOutput(new File(commandSplit[1]));
//    builder.redirectError(new File(commandSplit[1]));
//    Process p = builder.start(); // may throw IOException

    CommandIO out = DefaultCommadIO.of(CommandIO.prepareIO(command));
    out.setReturnCode(0);

    try {
      Process p = builder.start();

      p.waitFor();
      BufferedReader stdout =
          new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader stderr =
          new BufferedReader(new InputStreamReader(p.getErrorStream()));

      out.setCommandOutput(CommandIO.prepareIO(readProcessOutputs(stdout)));
      out.setCommandErrorOutput(CommandIO.prepareIO(readProcessOutputs(stderr)));

    } catch (Exception e) {
      out.setReturnCode(-1);
      out.setCommandErrorOutput(CommandIO.prepareIO(e.getMessage()));
    }

    return out;
  }

  private String readProcessOutputs(BufferedReader reader) throws IOException {
    StringBuffer output = new StringBuffer();
    String line = "";
    while ((line = reader.readLine()) != null) {
      output.append(line + "\n");
    }
    return output.toString();
  }

  private CommandIO executeSingleCommand(String command, Scope scope) {
    CommandIO output = DefaultCommadIO.of(CommandIO.prepareIO(command));
    String requestedCommand = command.split(" ")[0];
    Optional<ShellPlugin> plugin = scope.findShellPlugin(requestedCommand);
    if (plugin.isPresent()) {
      output = plugin.get().getCommand()
          .execute(output, Optional.of(scope));
    } else {
      throw new UnknownCommandException(requestedCommand);
    }
    return output;
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
   */
//  @Override
  public CommandIO executePipe(Scope scope, String command) {
    CommandIO out = DefaultCommadIO.of(CommandIO.prepareIO(command));
    List<Pair<Optional<ShellPlugin>, String>> list = preparePlugins(scope, command);
    validatePluginList(list);
    ExecutorListener temp = null;
    for (int i = list.size() - 1; i > -1; i--) {
      Pair<Optional<ShellPlugin>, String> item = list.get(i);
      PipeExecutor executor = PipeExecutor
          .of(item.getValue(), item.getKey().get(), scope, temp);
      temp = executor;
    }
    out = temp.run();
    return out;
  }

  private List<Pair<Optional<ShellPlugin>, String>> preparePlugins(Scope scope, String commands) {
    String[] splittedCommands = commands.split(" \\| ");
    return Arrays.stream(splittedCommands).map(c -> new Pair<>(scope.findShellPlugin(c), c))
        .collect(Collectors.toList());
  }

  private void validatePluginList(List<Pair<Optional<ShellPlugin>, String>> list) {
    list.forEach(pair -> {
      if (!pair.getKey().isPresent() || pair.getValue().length() < 1) {
        throw new RuntimeException();
      }
    });
  }
}

interface ExecutorListener {

  CommandIO finishedExecution(CommandIO output);

  void onError(CommandIO output);

  CommandIO run();
}

class PipeExecutor implements ExecutorListener {

  private ShellPlugin plugin;
  private String command;
  private Scope scope;
  private ExecutorListener listener;

  private PipeExecutor(String command, ShellPlugin plugin, Scope scope,
      ExecutorListener listener) {
    this.command = command;
    this.plugin = plugin;
    this.scope = scope;
    this.listener = listener;
  }

  public static PipeExecutor of(String command, ShellPlugin plugin, Scope scope,
      ExecutorListener listener) {
    return new PipeExecutor(command, plugin, scope, listener);
  }


  @Override
  public CommandIO finishedExecution(CommandIO output) {
    //FIXME : this is not correct!!!
//    Stream<String> stream = Stream.concat(Stream.of(command), output.getCommandOutput().get());
//    command = stream.findFirst().get();
	  StringBuilder sb = new StringBuilder();
	  sb.append(command+ " ");
	  output.getCommandOutput().get().forEach(item -> sb.append(item));
//    command = command + " " + output.getCommandOutput().get().reduce(String::concat).get();
	  command = sb.toString();
    return run();
  }

  @Override
  public void onError(CommandIO output) {
    throw new RuntimeException(
        "Return code : " + output.getReturnCode() + "\n" + output.getCommandErrorOutput().get()
            .findFirst());
  }

  @Override
  public CommandIO run() {
    CommandIO out = plugin.getCommand()
        .execute(DefaultCommadIO.of(CommandIO.prepareIO(command)), Optional.of(scope));
    if (out.getReturnCode() == 0) {
      if (listener != null) {
        out = listener.finishedExecution(out);
      } else {
        if (out.getCommandOutput().isPresent()) {
          return out;
        }
      }
    } else {
      return out;
    }
    return out;
  }
}
