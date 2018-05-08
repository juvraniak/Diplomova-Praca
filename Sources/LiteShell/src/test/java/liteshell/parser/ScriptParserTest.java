package liteshell.parser;

import java.io.File;
import liteshell.exceptions.MethodMissingEception;
import liteshell.executors.ScriptParser;
import org.junit.Test;

/**
 * @author xvraniak@stuba.sk
 */

public class ScriptParserTest {

  @Test
  public void testScriptParser() {

    String pathToScript =
        System.getProperty("user.dir") + File.separator + "src/test/resources/test1.lsh";
    ScriptParser scriptParser;
    try {
      scriptParser = new ScriptParser(pathToScript);
      scriptParser.parse();
    } catch (MethodMissingEception e) {
      e.printStackTrace();
    }

  }
}
