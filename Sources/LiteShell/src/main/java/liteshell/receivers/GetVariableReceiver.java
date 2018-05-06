package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.keywords.Keyword;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class GetVariableReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO output, String[] strings, Optional<Scope> optional) {

    output.setReturnCode(0);
    if (!optional.isPresent()) {
      throw new RuntimeException("Scope not present!");
    }
    Scope scope = optional.get();

    String variableName = strings[1]
        .substring(strings[1].indexOf("{") + 1, strings[1].indexOf("}"));

    Keyword key = scope.getScopeVariables().getInitializedVariables().get(variableName);
    if (key == null) {
      output.setReturnCode(-1);
      output.setCommandErrorOutput(Optional
          .of(Stream.of("Variable with name " + variableName + " was not initialized yet!")));
      return output;
    }
    switch (key) {
      case STRING:
        output.setCommandOutput(getString(variableName, scope));
        break;
      case INT:
        output.setCommandOutput(getInt(variableName, scope));
        break;
      case DOUBLE:
        output.setCommandOutput(getDouble(variableName, scope));
        break;
    }

    return output;
  }

  private Optional<Stream<String>> getDouble(String variableName, Scope scope) {
    return createOptStreamOfString(
        scope.getScopeVariables().getDoubleMap().get(variableName).toString());
  }

  private Optional<Stream<String>> getInt(String variableName, Scope scope) {
    return createOptStreamOfString(
        scope.getScopeVariables().getIntegerMap().get(variableName).toString());
  }

  private Optional<Stream<String>> getString(String variableName, Scope scope) {
    return createOptStreamOfString(scope.getScopeVariables().getStringMap().get(variableName));
  }

  private Optional<Stream<String>> createOptStreamOfString(String out) {
    return Optional.of(Stream.of(out));
  }
}
