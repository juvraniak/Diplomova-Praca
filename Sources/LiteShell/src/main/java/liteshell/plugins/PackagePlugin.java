package liteshell.plugins;

import java.util.Optional;
import liteshell.commands.Command;
import liteshell.commands.PackageCommand;
import liteshell.parsers.CommandParser;
import liteshell.parsers.FunctionParser;

/**
 * @author xvraniak@stuba.sk
 */

public class PackagePlugin implements ShellPlugin {

  @Override
  public PluginMeta getInfo() {
    return new PluginMeta("pkg", "1.0", Optional.empty());
  }

  @Override
  public Command getCommand() {
    return new PackageCommand();
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
