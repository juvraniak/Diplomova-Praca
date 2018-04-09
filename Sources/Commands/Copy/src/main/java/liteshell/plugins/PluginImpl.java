package liteshell.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import liteshell.commands.Command;
import liteshell.regex.ShellRegex;
import liteshell.commands.CopyCommand;


/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class PluginImpl implements ShellPlugin, ShellRegex {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "copy", createCopyRegex());
    }

    @Override
    public Command getCommand() {
        return new CopyCommand();
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    private Optional<List<String>> createCopyRegex() {
        return Optional.of(Arrays.asList("copy\\s{1}.+\\s{1}.+;"));
    }
}
