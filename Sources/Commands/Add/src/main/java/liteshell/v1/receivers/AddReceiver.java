package liteshell.v1.receivers;

import java.util.Optional;
import javafx.util.Pair;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.receivers.Receiver;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class AddReceiver implements Receiver {

  @Override
  public CommandOutput executeCommand(String[] strings, Optional<Scope> optional) {
    CommandOutput out = new DefaultOutput();
    out.setReturnCode(0);
    //TODO : figure this out!!!
    //add($(add(${i}, 5), 4)
    Pair<String, String> parsedPair = parseAdd(strings[0].substring(4, strings[0].length() - 1));
    String left = parsedPair.getKey();
    String right = parsedPair.getValue();
    return out;
  }

  private Pair<String, String> parseAdd(String substring) {
    String left = "";
    String right = "";
    StringBuilder sb = new StringBuilder();
    boolean isCommandOrVariable = substring.startsWith("$");
    int leftBraces = 0;
    for (char c : substring.toCharArray()) {
      sb.append(c);
    }
    return new Pair<>(sb.toString(), right);
  }


}
