package liteshell.v1.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import liteshell.commands.Command;
import liteshell.plugins.PluginMeta;
import liteshell.plugins.ShellPlugin;
import liteshell.v1.commands.ListDirectoryCommand;

/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class ListDirPlugin implements ShellPlugin {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "ls");
    }

    @Override
    public Command getCommand() {
        return new ListDirectoryCommand();
    }

    private Optional<List<String>> createListDirRegex() {
        return Optional.of(Arrays.asList("ls"));
    }
}
