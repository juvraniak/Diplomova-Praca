package liteshell.receivers;

import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;
import liteshell.utils.StringUtils;

/**
 * @author xvraniak@stuba.sk
 */

public class BooleanPrepReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings, Optional<Scope> optional) {
    String[] andOrSplit = StringUtils.removeEmptyStrings(strings[1].split("[||]|&&"));
    int size = andOrSplit.length;
    String andOrExpression[] = andOrSplit;
    if (size > 1) {
      andOrExpression = prepareAndOrExpression();
    }
    String regexp = ">|>=|==|!=|<=|<";
    commandIO.setReturnCode(0);
    commandIO.setCommandOutput(CommandIO.prepareIO("true"));
    return commandIO;
  }

  private String[] prepareAndOrExpression() {
  }
}
