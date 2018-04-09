package liteshell.receivers;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;

public class ListDirectoryReceiver implements Receiver {

    @Override
    public CommandOutput executeCommand(String[] args, Optional<Scope> scope) {
        CommandOutput commandOutput = new DefaultOutput();
        String directoryToList = gedDirectoryToList(args);
        File curDir = new File(directoryToList);
        File[] filesList = curDir.listFiles();

        try{
            Stream<String> out = Arrays.asList(filesList).stream().map(f -> f.getName()).sorted();
            commandOutput.setCommandOutput(out);
            commandOutput.setCommandErrorOutput(Stream.empty());
            commandOutput.setReturnCode(0);
        } catch (Exception e){
            commandOutput.setCommandErrorOutput(Stream.of(e.getMessage()));
            commandOutput.setReturnCode(-1);
        }

        return commandOutput;
    }

    private String gedDirectoryToList(String[] args) {
        if(args.length > 1){
            return args[1];
        }
        return System.getProperty("user.dir");
    }
}
