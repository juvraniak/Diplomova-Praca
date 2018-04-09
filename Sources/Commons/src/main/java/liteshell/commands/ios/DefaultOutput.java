package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xvraniak@stuba.sk
 */
@AllArgsConstructor
@NoArgsConstructor
public class DefaultOutput implements CommandOutput {

    private Integer returnValue;
    private Stream<String> output;
    private Stream<String> errorOutput;


    @Override
    public Optional<Integer> getReturnCode() {
        return Optional.of(returnValue);
    }

    @Override
    public Optional<Stream<String>> getCommandOutput() {
        return Optional.of(output);
    }

    @Override
    public Optional<Stream<String>> getCommandErrorOutput() {
        return Optional.of(errorOutput);
    }

    @Override
    public void setReturnCode(Integer returnCode) {
        returnValue = returnCode;
    }

    @Override
    public void setCommandOutput(Stream<String> commandOutput) {
        this.output = commandOutput;
    }

    @Override
    public void setCommandErrorOutput(Stream<String> commandErrorOutput) {
        this.errorOutput = commandErrorOutput;
    }
}
