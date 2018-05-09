package liteshell.parser;

import java.io.File;
import liteshell.exceptions.MethodMissingEception;
import liteshell.executors.RootParser;
import org.junit.Test;

/**
 * @author xvraniak@stuba.sk
 */

public class RootParserTest {

  @Test
  public void testScriptParser() {

    String pathToScript =
        System.getProperty("user.dir") + File.separator + "src/test/resources/test1.lsh";
    RootParser rootParser;
    try {
      rootParser = new RootParser(pathToScript);
      rootParser.parse();
    } catch (MethodMissingEception e) {
      e.printStackTrace();
    }

  }
}
