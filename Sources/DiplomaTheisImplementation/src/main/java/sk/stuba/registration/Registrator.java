package sk.stuba.registration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import sk.stuba.plugins.IntPlugin;
import terminal.common.command.Command;

/**
 * This class may serve as register as well as invoker
 */
public class Registrator {

    public Registrator(){
        registerAppCommands();
        registerCommands();
    }

    private void registerAppCommands() {
        JarLoader.shellCommands.add(new IntPlugin());
    }

    public Command getCommand(String command, String version) {
        return JarLoader.shellCommands.stream()
            .filter(shellPluging -> shellPluging.getInfo().getName().equals(command))
            .filter(shellPluging -> shellPluging.getInfo().getVersion().equals(version))
            .findFirst()
            .get()
            .load();
    }

    private void registerCommands() {
        List<Path> jars = new ArrayList<>();
        try {
//            jars = Files.list(Paths.get("/Users/jvraniak@sk.ibm.com/Documents/school/Diplomova-Praca/Sources/Commands/Libs/"))
//                .filter(Files::isRegularFile).collect(Collectors.toList());
            
            jars = Files.list(Paths.get("/home/jv/Documents/Skola/Diplomova-Praca/Sources/Commands/Libs"))
                    .filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        jars.forEach(path -> JarLoader.loadJar(path));

    }

}
