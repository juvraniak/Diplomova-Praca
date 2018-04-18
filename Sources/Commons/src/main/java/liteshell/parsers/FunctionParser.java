package liteshell.parsers;

/**
 * @author xvraniak@stuba.sk
 */

public interface FunctionParser extends CommandParser {

  String getStart();

  String getEnd();
}
