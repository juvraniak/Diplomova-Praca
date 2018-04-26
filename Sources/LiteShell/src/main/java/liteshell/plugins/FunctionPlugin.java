package liteshell.plugins;

import java.util.Optional;
import liteshell.commands.Command;
import liteshell.parsers.CommandParser;
import liteshell.parsers.FunctionParser;

/**
 * @author xvraniak@stuba.sk
 */

public class FunctionPlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("function", "1.0", Optional.empty());
  }

  @Override
  public Command getCommand() {
    return null;
  }

  @Override
  public boolean shouldPrint() {
    return false;
  }

  @Override
  public Optional<CommandParser> getCommandParser() {
    return Optional.empty();
  }

  @Override
  public Optional<FunctionParser> getFunctionParser() {
    return Optional.empty();
  }
}
