package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandOutput;
import liteshell.commands.ios.DefaultOutput;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeVariables;

/**
 * @author xvraniak@stuba.sk
 */

public class StringReceiver implements Receiver {

  final String[] metaCharacters = {"\\", " ", "\"", "^", "$", "{", "}", "[", "]", "(", ")", ".",
      "*",
      "+", "?",
      "|", "<", ">", "-", "&"};

  @Override
  public CommandOutput executeCommand(String[] strings, Optional<Scope> scope) {
    CommandOutput out = new DefaultOutput();
    boolean isScopePresent = scope.isPresent();
    String stringValue = "";

    String variableName = strings[1];

    if (isScopePresent && scope.get().getScopeVariables().getInitializedVariables()
        .contains(variableName)) {
      out.setCommandErrorOutput(
          Optional
              .of(Stream.of("Variable " + variableName + " is already defined in this scope!")));
      out.setReturnCode(-1);
      return out;
    }
    ScopeVariables var = scope.get().getScopeVariables();
    var.getInitializedVariables().add(variableName);
    var.getStringMap().put(variableName, stringValue);
    out.setReturnCode(0);
    if (strings.length > 2) {
      try {

        stringValue = escapeString(strings[2]);
        var.getStringMap().put(variableName, stringValue);
      } catch (NumberFormatException e) {
        out.setCommandErrorOutput(Optional.of(Stream.of("Wrong format : " + e.getMessage())));
        out.setReturnCode(-1);
      }

    }
    return out;
  }

  private String escapeString(String str) {
    for (int i = 0; i < metaCharacters.length; i++) {
      String meta = metaCharacters[i];
      if (str.contains(meta)) {
        str = str.replace(meta, "\\" + meta);
      }
    }
    return str;
  }
}
