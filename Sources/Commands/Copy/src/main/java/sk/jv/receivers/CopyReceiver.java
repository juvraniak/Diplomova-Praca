package sk.jv.receivers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

public class CopyReceiver implements Executable {

    @Override
    public void executeCommand(String[] args, Scope scope) {
        if(args.length != 3) {
            throw new IllegalArgumentException("Command takes exactly two arguments");
        }
        Path sourcePath = Paths.get(args[1]);
        Path destPath = Paths.get(args[2]);
        try {
            Files.copy(sourcePath, destPath);
        } catch (IOException e) {
            System.out.println("Unable to copy specified files - try to force coping");
        }
    }
}
