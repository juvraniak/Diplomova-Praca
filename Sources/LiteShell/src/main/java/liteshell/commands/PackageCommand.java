package liteshell.commands;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
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
  public CommandIO execute(CommandIO commandInput, Optional<Scope> scope) {
    if (commandInput.getCommandInput().isPresent()) {
      String[] splitedImport = parseComand(commandInput.getCommandInput().get());
      String commandCase = splitedImport[1];

      switch (commandCase) {
        case "download":
          commandInput = new DownloadPackageReceiver()
              .executeCommand(commandInput, splitedImport, scope);
          break;
        case "change":
          commandInput = new ChangePackageReceiver()
              .executeCommand(commandInput, splitedImport, scope);
          break;
        case "delete":
          commandInput = new DeletePackageReceiver()
              .executeCommand(commandInput, splitedImport, scope);
          break;
      }
      return commandInput;
    } else {
      throw new CommandIOException("Input not found");
    }
  }

  @Override
  public String[] parseComand(Stream<String> stream) {
    return stream.findFirst().get().split(" ");
  }
}
