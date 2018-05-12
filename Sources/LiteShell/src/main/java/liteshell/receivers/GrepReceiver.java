package liteshell.receivers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class GrepReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings,
      Optional<Scope> optional) {

    String filterWord = strings[1];
    //TODO: will have to cover scenario when grep parameter is not string to grep but file to grep.
    List<String> listToGrep = Arrays.asList(strings[2].split("\n"));
    try {

      StringBuilder sb = new StringBuilder();

      listToGrep.stream().filter(item -> item.contains(filterWord)).forEach(item -> sb.append("\n" + item));

      commandIO.setCommandOutput(CommandIO.prepareIO(sb.toString()));
      commandIO.setCommandErrorOutput(Optional.of(Stream.empty()));
      commandIO.setReturnCode(0);
    } catch (Exception e) {
      commandIO.setCommandErrorOutput(Optional.of(Stream.of(e.getMessage())));
      commandIO.setReturnCode(-1);
    }

    return commandIO;
  }
}
