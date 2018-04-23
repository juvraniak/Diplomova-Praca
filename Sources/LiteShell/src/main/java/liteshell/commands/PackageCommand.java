package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandInput;
import liteshell.commands.ios.CommandOutput;
import liteshell.exceptions.CommandIOException;
import liteshell.receivers.ChangePackageReceiver;
import liteshell.receivers.DeletePackageReceiver;
import liteshell.receivers.DownloadPackageReceiver;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class PackageCommand implements Command {


  @Override
  public CommandOutput execute(CommandInput commandInput, Optional<Scope> scope) {
    if (commandInput.getCommandInput().isPresent()) {
      String[] splitedImport = parseComand(commandInput.getCommandInput().get());
      String commandCase = splitedImport[1];
      CommandOutput out = null;
      switch (commandCase) {
        case "download":
          out = new DownloadPackageReceiver().executeCommand(splitedImport, scope);
          break;
        case "change":
          out = new ChangePackageReceiver().executeCommand(splitedImport, scope);
          break;
        case "delete":
          out = new DeletePackageReceiver().executeCommand(splitedImport, scope);
          break;
      }
      return out;
    } else {
      throw new CommandIOException("Input not found");
    }
  }

  @Override
  public String[] parseComand(Stream<String> stream) {
    return stream.findFirst().get().split(" ");
  }
}
