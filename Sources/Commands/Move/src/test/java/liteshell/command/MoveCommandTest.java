package liteshell.command;

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
import liteshell.test.PluginTest;
import liteshell.plugins.PluginImpl;


public class MoveCommandTest implements PluginTest{

    PluginImpl movePlugin = new PluginImpl();

    @Test
    @Override
    public void regexTest() {
        Optional<List<String>> patterns = movePlugin.getInfo().getMatcher();
        Assert.assertTrue("Matcher list is empty!", patterns.isPresent());
        List<String> correctCommand = Arrays.asList("move path path;");
        List<String> incorrectCommand = Arrays.asList("move path path");

        assertRegex(patterns, correctCommand, incorrectCommand);
    }
    @Test
    @Override
    public void commandTest() {
        Command moveCommand = movePlugin.getCommand();
        Assert.assertNotNull(moveCommand);

        String fileLocation = System.getProperty("user.dir")+File.separator;
        String fileToCopy = fileLocation + "toCopy.txt";
        String copied = fileLocation + "copied.txt";

        String text = "The first line";
        Path file = Paths.get(fileToCopy);
        try {
            Files.write(file, Collections.singleton(text), Charset.forName("UTF-8"));
            moveCommand.execute(new DefaultInput(Stream.of("move " + fileToCopy + " " + copied)), Optional.empty());
            Assert.assertNotNull(new File(copied));
            Assert.assertTrue(new String(Files.readAllBytes(Paths.get(copied))).contains(text));
            Files.delete(Paths.get(copied));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
