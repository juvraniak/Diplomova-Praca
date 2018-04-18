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

import org.junit.Assert;
import org.junit.Test;

import liteshell.commands.Command;
import liteshell.commands.ios.DefaultInput;
import liteshell.plugins.RemovePlugin;
import liteshell.test.PluginTest;

public class RemoveCommandTest implements PluginTest{

    RemovePlugin copyPlugin = new RemovePlugin();

    @Test
    @Override
    public void regexTest() {
        Optional<List<String>> patterns = copyPlugin.getInfo().getMatcher();
        Assert.assertTrue("Matcher list is empty!", patterns.isPresent());
        List<String> correctCommand = Arrays.asList("rm path;");
        List<String> incorrectCommand = Arrays.asList("cp path path");

        assertRegex(patterns, correctCommand, incorrectCommand);
    }
    @Test
    @Override
    public void commandTest() {
        Command command = copyPlugin.getCommand();
        Assert.assertNotNull(command);

        String fileLocation = System.getProperty("user.dir")+File.separator;
        String toDelete = fileLocation + "toDelete.txt";
        String toDelete1 = fileLocation + "toDelete1.txt";

        String text = "The first line";
        Path path1 = Paths.get(toDelete);
        Path path2 = Paths.get(toDelete1);
        try {
            Files.write(path1, Collections.singleton(text), Charset.forName("UTF-8"));
            Files.write(path2, Collections.singleton(text), Charset.forName("UTF-8"));
            command.execute(DefaultInput.of(Stream.of("rm " + toDelete + " " + toDelete1)), Optional.empty());
            command.execute(DefaultInput.of(Stream.of("rm " + toDelete + " " + toDelete1)), Optional.empty());
            Assert.assertNull(new File(toDelete));
            Assert.assertNull(new File(toDelete1));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
