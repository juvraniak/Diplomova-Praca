package receivers;

import commandcommons.CommandOutput;
import scopes.ApplicationScope;

import java.util.HashMap;
import java.util.Map;

public class DoubleReceiver implements Receiver {
    @Override
    public CommandOutput receiveAndExecute(String[] parsedCommand, ApplicationScope scope) {

        if (parsedCommand.length != 0 && scope != null && parsedCommand[0].equals("int")) {
            if (scope.getScopeData().getIntegerMap() != null) {
                //predpoklad ze vsetky veci mame zadanie teda int i = 4;
                scope.getScopeData().getIntegerMap().put(parsedCommand[1], Integer.parseInt(parsedCommand[3]));
            } else {
                Map<String, Integer> temp = new HashMap<>();
                temp.put(parsedCommand[1], Integer.parseInt(parsedCommand[3]));
                scope.getScopeData().setIntegerMap(temp);
            }

        } else {
            throw new RuntimeException("Too many parameters or cannot create this parameter in scope " + scope);
        }
        return null;
    }
}
