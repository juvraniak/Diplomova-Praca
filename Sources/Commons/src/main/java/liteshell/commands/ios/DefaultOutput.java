package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;

/**
 * @author xvraniak@stuba.sk
 */
@AllArgsConstructor
public class DefaultOutput implements CommandOutput {

    private Integer returnValue;
    private Optional<Stream<String>> output;
    private Optional<Stream<String>> errorOutput;

    public DefaultOutput() {
        returnValue = 0;
        output = Optional.empty();
        errorOutput = Optional.empty();
    }

    @Override
    public Integer getReturnCode() {
        return returnValue;
    }

    @Override
    public Optional<Stream<String>> getCommandOutput() {
        return output;
    }

    @Override
    public Optional<Stream<String>> getCommandErrorOutput() {
        return errorOutput;
    }

    @Override
    public void setReturnCode(Integer returnCode) {
        returnValue = returnCode;
    }

    @Override
    public void setCommandOutput(Optional<Stream<String>> commandOutput) {
        this.output = commandOutput;
    }

    @Override
    public void setCommandErrorOutput(Optional<Stream<String>> commandErrorOutput) {
        this.errorOutput = commandErrorOutput;
    }
}
