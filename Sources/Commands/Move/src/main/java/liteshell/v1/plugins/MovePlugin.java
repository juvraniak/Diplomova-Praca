package liteshell.v1.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import liteshell.commands.Command;
import liteshell.plugins.PluginMeta;
import liteshell.plugins.ShellPlugin;
import liteshell.v1.commands.MoveCommand;

/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class MovePlugin implements ShellPlugin {

    @Override
    public PluginMeta getInfo() {
      return new PluginMeta("1.0", "move");
    }

    @Override
    public Command getCommand() {
        return new MoveCommand();
    }

    private Optional<List<String>> createMoveRegex(){
        return Optional.of(Arrays.asList("move\\s{1}.+\\s{1}.+;"));
    }

}
