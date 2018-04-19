package liteshell.receivers;

import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xvraniak@stuba.sk
 */

public class GrepReceiver implements Receiver {
    @Override
    public CommandOutput executeCommand(String[] strings, Optional<Scope> optional) {
        CommandOutput commandOutput = new DefaultOutput();

        String filterWord = strings[1];
        //TODO: will have to cover scenario when grep parameter is not string to grep but file to grep.

        try{
            Stream<String> out = Arrays.asList(strings).stream().skip(2).filter(item -> item.contains(filterWord)).sorted();
            commandOutput.setCommandOutput(out);
            commandOutput.setCommandErrorOutput(Stream.empty());
            commandOutput.setReturnCode(0);
        } catch (Exception e){
            commandOutput.setCommandErrorOutput(Stream.of(e.getMessage()));
            commandOutput.setReturnCode(-1);
        }

        return commandOutput;
    }
}