import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.Command;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultInput;
import liteshell.v1.plugins.RemovePlugin;
import liteshell.test.PluginTest;
import org.junit.Assert;
import org.junit.Test;

public class RemoveCommandTest implements PluginTest {

  RemovePlugin removePlugin = new RemovePlugin();

  @Test
  @Override
  public void regexTest() {
    Optional<List<String>> patterns = removePlugin.getInfo().getMatcher();
    Assert.assertTrue("Matcher list is empty!", patterns.isPresent());
    List<String> correctCommand = Arrays.asList("rm path;");
    List<String> incorrectCommand = Arrays.asList("cp path path");

    assertRegex(patterns, correctCommand, incorrectCommand);
  }

  @Test
  @Override
  public void commandTest() {
    Command command = removePlugin.getCommand();
    Assert.assertNotNull(command);

    String fileLocation = System.getProperty("user.dir") + File.separator;
    String toDelete = fileLocation + "toDelete.txt";
    String toDelete1 = fileLocation + "toDelete1.txt";

    String text = "The first line";
    Path path1 = Paths.get(toDelete);
    Path path2 = Paths.get(toDelete1);
    CommandOutput out = null;
    try {
      Files.write(path1, Collections.singleton(text), Charset.forName("UTF-8"));
      Files.write(path2, Collections.singleton(text), Charset.forName("UTF-8"));
      out = command.execute(DefaultInput.of(Stream.of("rm " + toDelete)),
          Optional.empty());
      Assert.assertTrue(out.getReturnCode() == 0);
      out = command.execute(DefaultInput.of(Stream.of("rm " + toDelete1)),
          Optional.empty());
      Assert.assertTrue(out.getReturnCode() == 0);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
