package liteshell.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import liteshell.commands.Command;

/**
 * @author xvraniak@stuba.sk
 */

public class PipePlugin implements ShellPlugin {

    @Override
    public PluginMeta getInfo() {
        return new PluginMeta("1.0", "|", Optional.of(createPipeRegex()));
    }

    @Override
    public Command getCommand() {
        return null;
    }

    private List<String> createPipeRegex() {
        return Arrays.asList(
            "^.+\\s*([|]\\s*.+)+[;]"
        );
    }
}
