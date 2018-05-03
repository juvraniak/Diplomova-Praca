package liteshell.command;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.Command;
import liteshell.commands.ios.DefaultInput;
import liteshell.test.PluginTest;
import liteshell.v1.plugins.CopyPlugin;
import org.junit.Assert;
import org.junit.Test;

public class CopyCommandTest implements PluginTest{

    CopyPlugin copyPlugin = new CopyPlugin();

    @Test
    @Override
    public void commandTest() {
        Command copyCommand = copyPlugin.getCommand();
        Assert.assertNotNull(copyCommand);

        String fileLocation = System.getProperty("user.dir")+File.separator;
        String fileToCopy = fileLocation + "toCopy.txt";
        String copied = fileLocation + "copied.txt";

        String text = "The first line";
        Path file = Paths.get(fileToCopy);
        try {
            Files.write(file, Collections.singleton(text), Charset.forName("UTF-8"));
            copyCommand.execute(DefaultInput.of(Stream.of("copy " + fileToCopy + " " + copied)), Optional.empty());
            Assert.assertNotNull(new File(copied));
            Assert.assertTrue(new String(Files.readAllBytes(Paths.get(copied))).contains(text));
            Files.delete(Paths.get(fileToCopy));
            Files.delete(Paths.get(copied));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
