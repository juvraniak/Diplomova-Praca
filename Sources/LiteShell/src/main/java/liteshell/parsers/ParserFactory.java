package liteshell.parsers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import liteshell.plugins.ShellPlugin;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xvraniak@stuba.sk
 */

public class ParserFactory {

  private static ParserFactory INSTANCE = new ParserFactory();

  @Getter
  @Setter
  private Map<String, CommandParser> commandParsers;
  @Getter
  @Setter
  private Map<String, FunctionParser> functionParsers;


  private ParserFactory() {
    this.commandParsers = new HashMap<>();
    this.functionParsers = new HashMap<>();
  }

  public static ParserFactory init() {
    return INSTANCE;
  }

  public void addParsers(Map<String, ShellPlugin> shellPlugins) {
    shellPlugins.forEach((command, plugin) -> {
      Optional<CommandParser> commandParser = plugin.getCommandParser();
      Optional<FunctionParser> functionParser = plugin.getFunctionParser();
      if (commandParser.isPresent()) {
        commandParsers.put(command, commandParser.get());
      }
      if (functionParser.isPresent()) {
        functionParsers.put(command, functionParser.get());
      }
    });
  }
}

