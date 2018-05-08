package liteshell.arithmetic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeImpl;
import liteshell.utils.ShellClient;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author xvraniak@stuba.sk
 */

public class ShittyArithmeticTest {

  @Test
  public void testScriptParser() {

    String pathToScript =
        System.getProperty("user.dir") + File.separator + "src/test/resources/arithmetic.lsh";
    StringBuilder sb = new StringBuilder();
    Scope s = new ScopeImpl("test", ShellClient.getInstance(), null);
    try (Stream<String> lines = Files.lines(Paths.get(pathToScript), Charset.defaultCharset())) {
      List<String> collect = lines.collect(Collectors.toList());
      collect.stream().forEach(l -> {
        s.getExecutor().execute(l.trim(), s);
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

    Assert.assertTrue(88 == s.getScopeVariables().getIntegerMap().get("i"));
    Assert.assertTrue(4 == s.getScopeVariables().getIntegerMap().get("k"));
    Assert.assertTrue(4 == s.getScopeVariables().getIntegerMap().get("h"));
    Assert.assertTrue(s.getScopeVariables().getStringMap().get("shit").equals("shit"));
    Assert.assertTrue(s.getScopeVariables().getStringMap().get("str").equals("dsadadasdasdshit"));
    Assert.assertTrue(s.getScopeVariables().getStringMap().get("str2").equals("shit"));

  }
}
