package liteshell.v1.receivers;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;

public class ListDirectoryReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] args, Optional<Scope> scope) {

    String directoryToList = gedDirectoryToList(args, scope);
    File curDir = new File(directoryToList);
    File[] filesList = curDir.listFiles();

    try {
      String collectedFoldersFiles = Arrays.asList(filesList).stream()
          .map(f -> f.getName() + "\n").sorted().collect(Collectors.toList())
          .stream().reduce(String::concat).get();
      commandIO.setCommandOutput(CommandIO.prepareIO(collectedFoldersFiles));
      commandIO.setReturnCode(0);
    } catch (Exception e) {
      commandIO.setCommandErrorOutput(Optional.of(Stream.of(e.getMessage())));
      commandIO.setReturnCode(-1);
    }

    return commandIO;
  }

  private String gedDirectoryToList(String[] args, Optional<Scope> scope) {
    if (args.length > 1) {
      return args[1];
    }
    if (scope.isPresent()) {
      return scope.get().getCurrentWorkingDirectory();
    }
    return System.getProperty("user.home");
  }
}
