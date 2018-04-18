package liteshell.parser;

import java.io.File;
import liteshell.executors.ScriptParser;
import org.junit.Test;

/**
 * @author xvraniak@stuba.sk
 */

public class ScriptParserTest {

  @Test
  public void testScriptParser() {
    ScriptParser scriptParser = new ScriptParser();
    String pathToScript =
        System.getProperty("user.dir") + File.separator + "src/test/resources/test1.ls";
    scriptParser.parse(pathToScript);
  }
}
