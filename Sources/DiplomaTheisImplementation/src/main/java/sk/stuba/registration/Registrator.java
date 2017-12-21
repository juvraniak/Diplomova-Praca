package sk.stuba.registration;

import java.util.HashMap;
import java.util.Map;

import sk.stuba.commands.Command;
import sk.stuba.commands.CopyCommand;
import sk.stuba.commands.ListDirectoryCommand;
import sk.stuba.commands.MoveCommand;

/**
 * This class may serve as register as well as invoker
 */
public class Registrator {

    Map<String, Command> commandMap;

    public Registrator(){
        commandMap= new HashMap<>();
        commandMap.put("cp", new CopyCommand());
        commandMap.put("mv", new MoveCommand());
        commandMap.put("ls", new ListDirectoryCommand());
    }

    public Command getCommand(String command){
        return commandMap.get(command);
    }

}
