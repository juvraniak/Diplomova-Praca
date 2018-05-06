package liteshell.commands.ios;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xvraniak@stuba.sk
 */

public class DefaultCommadIO implements CommandIO {

  private final Optional<Stream<String>> input;
  private Integer returnValue;
  private Optional<Stream<String>> output;
  private Optional<Stream<String>> errorOutput;

  private DefaultCommadIO() {
    this(Optional.empty());
  }

  private DefaultCommadIO(Optional<Stream<String>> input) {
    this.input = input;
    returnValue = 0;
    output = Optional.empty();
    errorOutput = Optional.empty();
  }

  @Override
  public Optional<Stream<String>> getCommandInput() {
    return input;
  }

  public static DefaultCommadIO of(Optional<Stream<String>> input) {
    return new DefaultCommadIO(input);
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
