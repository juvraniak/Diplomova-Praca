package liteshell.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import liteshell.commands.CopyCommand;
import liteshell.commands.Command;
import liteshell.regex.ShellRegex;


/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */

public class PluginImpl implements ShellPlugin, ShellRegex {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "rm", createRegex());
    }

    @Override
    public Command getCommand() {
        return new CopyCommand();
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    private Optional<List<String>> createRegex() {
        return Optional.of(Arrays.asList("rm\\s{1}.+;?"));
    }
}
