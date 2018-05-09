package liteshell.command;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import liteshell.commands.Command;
import liteshell.commands.ios.CommandIO;
import liteshell.commands.ios.DefaultCommadIO;
import liteshell.test.PluginTest;
import liteshell.v1.plugins.MovePlugin;
import org.junit.Assert;
import org.junit.Test;


public class MoveCommandTest implements PluginTest{

    MovePlugin movePlugin = new MovePlugin();

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
            moveCommand.execute(
                DefaultCommadIO.of(CommandIO.prepareIO("move " + fileToCopy + " " + copied)),
                Optional.empty());
            Assert.assertNotNull(new File(copied));
            Assert.assertTrue(new String(Files.readAllBytes(Paths.get(copied))).contains(text));
            Files.delete(Paths.get(copied));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
