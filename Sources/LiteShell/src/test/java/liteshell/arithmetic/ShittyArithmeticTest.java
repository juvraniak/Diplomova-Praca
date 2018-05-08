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

  @Test
  public void secondShittyTest() {
    String line;
    Scope s = new ScopeImpl("test", ShellClient.getInstance(), null);

    line = "$(int k = 4);";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(4 == s.getScopeVariables().getIntegerMap().get("k"));

    line = "$(int i = $(${k}+4+5+6-4+6*9-7/2+3-6*5/5));";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(88 == s.getScopeVariables().getIntegerMap().get("i"));

    line = "$(int h = ${k});";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(4 == s.getScopeVariables().getIntegerMap().get("h"));

    line = "$(${h} = 3);";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(3 == s.getScopeVariables().getIntegerMap().get("h"));

    line = "$(${h} = $(${k}+4+5+6-4+6*9-7/2+3-6*5/5));";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(88 == s.getScopeVariables().getIntegerMap().get("h"));

    line = "$(${h} = ${k});";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(4 == s.getScopeVariables().getIntegerMap().get("h"));

    line = "$(string shit = \"shit\");";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getStringMap().get("shit").equals("shit"));

    line = "$(string str = $(\"dsada\"+\"dasdasd\"+${shit}));";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getStringMap().get("str").equals("dsadadasdasdshit"));

    line = "$(string str2 = ${shit});";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getStringMap().get("str2").equals("shit"));

    line = "$(${str2} = \"cosi\");";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getStringMap().get("str2").equals("cosi"));

    line = "$(${str2} = ${shit});";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getStringMap().get("str2").equals("shit"));

    line = "$(boolean b = true);";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getBooleanMap().get("b"));

    line = "$(${b} = false);";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertFalse(s.getScopeVariables().getBooleanMap().get("b"));

    line = "$(boolean b1 = $($(${k}==${h})&&true||$(${k}<${h})));";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getBooleanMap().get("b1"));

    line = "$(boolean b1 = $($(${k}==${h})));";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getBooleanMap().get("b1"));

    line = "$(${b} = $(${k}!=${h}));";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertFalse(s.getScopeVariables().getBooleanMap().get("b"));

    line = "$(${b} = $(${k}!=${i}));";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getBooleanMap().get("b"));

    line = "$(boolean bshit = $(true==true));";
    s.getExecutor().execute(line.trim(), s);
    Assert.assertTrue(s.getScopeVariables().getBooleanMap().get("bshit"));
  }
}
