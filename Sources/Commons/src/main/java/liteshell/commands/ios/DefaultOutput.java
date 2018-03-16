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

    private Optional<Stream<String>> output;
    private Optional<Stream<String>> errorOutput;


    @Override
    public Optional<Stream<String>> getCommandOutput() {
        return output;
    }

    @Override
    public Optional<Stream<String>> getCommandErrorOutput() {
        return errorOutput;
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
