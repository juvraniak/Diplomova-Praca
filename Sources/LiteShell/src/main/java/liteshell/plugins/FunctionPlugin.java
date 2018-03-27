package liteshell.plugins;

import java.util.Arrays;
import java.util.Optional;

import liteshell.commands.Command;
import liteshell.commands.FunctionCommand;
import liteshell.regex.ShellRegex;

/**
 * @author xvraniak@stuba.sk
 */

public class FunctionPlugin implements ShellPlugin, ShellRegex {

    @Override
    public PluginMeta getInfo() {
        return createFunctionPluginMeta();
    }

    @Override
    public Command getCommand() {
        return new FunctionCommand();
    }

    private PluginMeta createFunctionPluginMeta() {
        PluginMeta pluginMeta = new PluginMeta();
        pluginMeta.setName("function");
        pluginMeta.setVersion("1");
        pluginMeta.setMatcher(Optional.of(Arrays.asList(
            "function " + types() + " \\D.{0,20}[(](" + types() + " \\D" + charNums() + "{0,20}){0,1}(,\\s*" + types() + " \\D" + charNums()
                + "{0,20})*[)]\\s*[{]\\s*\n" + line() + "[}]\n*")));
        return pluginMeta;
    }

}
