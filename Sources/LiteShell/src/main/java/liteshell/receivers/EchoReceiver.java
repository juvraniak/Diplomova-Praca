package liteshell.receivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class EchoReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings, Optional<Scope> optional) {
    List<String> expression = parseInput(strings[0], optional.get()).stream()
        .map(s -> {
          boolean isCommand = s.startsWith("$(");
          boolean isVar = s.startsWith("${");
          return findValue(s, isCommand, isVar, optional.get());
        }).collect(Collectors.toList());
    commandIO.setReturnCode(0);
    commandIO.setCommandOutput(CommandIO.prepareIO(expression.toString()));
    return commandIO;
  }

  private List<String> parseInput(String toParse, Scope scope) {
    List<String> strings = new ArrayList<>();
    boolean isCommand = false;
    boolean isVar = false;

    String toSave = "";
    for (int i = 0; i < toParse.length(); ) {
      toSave = "";
      char c = 'f';
      try {
        c = toParse.charAt(i);
      } catch (Exception e) {

      }
      if (c == '"') {

        toSave = readString(toParse, i, '"');

      } else if (c == '$') {
        c = toParse.charAt(i + 1);
        if (c == '(') {
          isCommand = true;
          toSave = readString(toParse, i, ')');
        } else if (c == '{') {
          isVar = true;
          toSave = readString(toParse, i, '}');
        }
      } else if (toParse.length() >= i + 4 && toParse.substring(i, i + 4)
          .matches("true")) {
        toSave = toParse.substring(i, i + 4);
      } else if (toParse.length() >= i + 4 && toParse.substring(i, i + 5)
          .matches("false")) {
        toSave = toParse.substring(i, i + 4);
      } else if (c == 'f') {
        break;
      }
      i += toSave.length() + 1;
      strings.add(toSave);
    }
    return strings;
  }

  private String readString(String toParse, int i, char c) {
    return toParse.substring(i, toParse.indexOf(c, i + 1) + 1);
  }
}
