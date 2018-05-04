package liteshell.commands;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandInput;
import liteshell.commands.ios.CommandOutput;
import liteshell.exceptions.CommandIOException;
import liteshell.receivers.ChangeVariableReceiver;
import liteshell.receivers.DoubleReceiver;
import liteshell.receivers.GetVariableReceiver;
import liteshell.receivers.IntReceiver;
import liteshell.receivers.StringReceiver;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class VariableCommand implements Command {

  @Override
  public CommandOutput execute(CommandInput commandInput, Optional<Scope> scope) {
    if (commandInput.getCommandInput().isPresent()) {
      String[] splitedImport = parseComand(commandInput.getCommandInput().get());
      String commandCase = splitedImport[0];
      CommandOutput out = null;
      switch (commandCase) {
        case "int":
          out = new IntReceiver().executeCommand(splitedImport, scope);
          break;
        case "double":
          out = new DoubleReceiver().executeCommand(splitedImport, scope);
          break;
        case "boolean":
//                    out = new DeletePackageReceiver().executeCommand(splitedImport, scope);
          break;
        case "string":
          out = new StringReceiver().executeCommand(splitedImport, scope);
          break;
        case "change":
          out = new ChangeVariableReceiver().executeCommand(splitedImport, scope);
          break;
        case "get":
          out = new GetVariableReceiver().executeCommand(splitedImport, scope);
          break;
      }
      return out;
    } else {
      throw new CommandIOException("Input not found");
    }
  }

  @Override
  public String[] parseComand(Stream<String> stream) {
    String input = stream.findFirst().get();
    if (input.endsWith(";")) {
      input = input.substring(0, input.length() - 1);
    }
    if (input.startsWith("${")) {
      if (input.contains(" = ")) {
        String[] eqSplit = removeEmptyStrings(input.split(" = "));
        String[] out = new String[]{"change", eqSplit[0], eqSplit[1]};
        return out;
      } else {
        String[] out = new String[]{"get", input};
        return out;
      }
    }
    if (input.contains(" = ")) {
      String[] eqSplit = removeEmptyStrings(input.split(" = "));
      String[] out = new String[]{eqSplit[0].split(" ")[0], eqSplit[0].split(" ")[1],
          eqSplit[1]};
      return out;
    }
    String[] out = new String[]{input.split(" ")[0], input.split(" ")[1]};
    return out;
  }

  private String[] removeEmptyStrings(String[] split) {
    return Arrays.asList(split).stream().filter(s -> !s.isEmpty()).toArray(String[]::new);
  }
}
