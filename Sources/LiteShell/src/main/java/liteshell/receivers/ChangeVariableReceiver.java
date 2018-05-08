package liteshell.receivers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.keywords.Keyword;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ChangeVariableReceiver implements Receiver {

  final String[] metaCharacters = {"\""};

  @Override
  public CommandIO executeCommand(CommandIO output, String[] strings, Optional<Scope> optional) {

    output.setReturnCode(0);

    if (!optional.isPresent()) {
      throw new RuntimeException("Scope not present!");
    }
    Scope scope = optional.get();

    String variableName = strings[1]
        .substring(strings[1].indexOf("{") + 1, strings[1].indexOf("}"));

    boolean isCommand = strings[2].startsWith("$(");
    boolean isInitializedVariable = strings[2].startsWith("${");
    String replacement;

    Keyword key = scope.getScopeVariables().getInitializedVariables().get(variableName);
    if (key == null) {
      output.setReturnCode(-1);
      output.setCommandErrorOutput(Optional
          .of(Stream.of("Variable with name " + variableName + " was not initialized yet!")));
      return output;
    }
    replacement = isCommand ? resolveChange(strings[2], key) : strings[2];
    replacement = findValue(replacement, isCommand, isInitializedVariable, scope);

    switch (key) {
      case STRING:
        changeString(variableName, replacement, scope);
        break;
      case INT:
        changeInt(variableName, replacement, scope);
        break;
      case DOUBLE:
        changeDouble(variableName, replacement, scope);
        break;
      case BOOLEAN:
        changeBoolean(variableName, replacement, scope);
        break;
    }

    return output;
  }


  private String resolveChange(String string, Keyword key) {
    switch (key) {
      case INT:
      case DOUBLE:
        return "$(arithmetic " + string
            .substring(string.indexOf("(") + 1, string.length());
      case STRING:
        return "$(stringsPrep " + string
            .substring(string.indexOf("(") + 1, string.length());
      case BOOLEAN:
        return "$(booleanPrep " + string
            .substring(string.indexOf("(") + 1, string.length());
      default:
        return string;
    }
  }

  private void changeInt(String variableName, String string, Scope scope) {
    BigDecimal bd = new BigDecimal(string);
    scope.getScopeVariables().getIntegerMap().put(variableName, bd.intValue());
  }

  private void changeDouble(String variableName, String string, Scope scope) {
    BigDecimal bd = new BigDecimal(string);
    scope.getScopeVariables().getDoubleMap().put(variableName, bd.doubleValue());
  }

  private void changeString(String variableName, String string, Scope scope) {
    scope.getScopeVariables().getStringMap().put(variableName, escapeString(string));
  }

  private void changeBoolean(String variableName, String replacement, Scope scope) {
    Boolean bool = replacement.equals("true") ? true : replacement.equals("false") ? false : null;
    if (bool != null) {
      scope.getScopeVariables().getBooleanMap().put(variableName, bool);
    }
  }

  private String escapeString(String str) {
    if (str.startsWith("\"") && str.endsWith("\"")) {
      str = str.substring(1, str.length() - 1);
    }
    for (int i = 0; i < metaCharacters.length; i++) {
      String meta = metaCharacters[i];
      if (str.contains(meta)) {
        str = str.replace(meta, "\\" + meta);
      }
    }
    return str;
  }
}
