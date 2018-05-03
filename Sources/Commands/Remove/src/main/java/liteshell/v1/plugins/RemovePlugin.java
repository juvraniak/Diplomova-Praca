package liteshell.v1.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import liteshell.commands.Command;
import liteshell.plugins.PluginMeta;
import liteshell.plugins.ShellPlugin;
import liteshell.v1.commands.CopyCommand;


/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class RemovePlugin implements ShellPlugin {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "rm");
    }

    @Override
    public Command getCommand() {
        return new CopyCommand();
    }

    private Optional<List<String>> createRegex() {
        return Optional.of(Arrays.asList("rm\\s{1}.+;?"));
    }
}
