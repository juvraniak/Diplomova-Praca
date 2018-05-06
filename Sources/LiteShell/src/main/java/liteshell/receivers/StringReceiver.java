package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.keywords.Keyword;
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
  public CommandIO executeCommand(CommandIO out, String[] strings, Optional<Scope> scope) {
    boolean isScopePresent = scope.isPresent();
    String stringValue = "";

    String variableName = strings[1];

    if (isScopePresent && scope.get().getScopeVariables().getInitializedVariables()
        .get(variableName) != null) {
      out.setCommandErrorOutput(
          Optional
              .of(Stream.of("Variable " + variableName + " is already defined in this scope!")));
      out.setReturnCode(-1);
      return out;
    }
    ScopeVariables var = scope.get().getScopeVariables();
    var.getInitializedVariables().put(variableName, Keyword.STRING);
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
