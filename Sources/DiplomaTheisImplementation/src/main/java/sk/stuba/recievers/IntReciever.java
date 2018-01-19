package sk.stuba.recievers;

import java.util.HashMap;
import java.util.Map;

import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class IntReciever implements Executable {

    @Override
    public void executeCommand(String[] strings, Scope scope) {
        if (strings.length != 0 && scope != null && strings[0].equals("int")) {
            if (scope.getScopeData().getIntegerMap() != null) {
                //predpoklad ze vsetky veci mame zadanie teda int i = 4;
                scope.getScopeData().getIntegerMap().put(strings[1], Integer.parseInt(strings[3]));
            } else {
                Map<String, Integer> temp = new HashMap<>();
                temp.put(strings[1], Integer.parseInt(strings[3]));
                scope.getScopeData().setIntegerMap(temp);
            }
        } else {
            throw new RuntimeException("Too many parameters or cannot create this parameter in scope " + scope);
        }
    }
}
