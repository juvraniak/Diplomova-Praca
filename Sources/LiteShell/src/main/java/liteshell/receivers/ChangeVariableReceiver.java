package liteshell.receivers;

import java.util.Optional;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.keywords.Keyword;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ChangeVariableReceiver implements Receiver {

  final String[] metaCharacters = {"\\", " ", "\"", "^", "$", "{", "}", "[", "]", "(", ")", ".",
      "*",
      "+", "?",
      "|", "<", ">", "-", "&"};

  @Override
  public CommandIO executeCommand(CommandIO output, String[] strings, Optional<Scope> optional) {

    output.setReturnCode(0);

    if (!optional.isPresent()) {
      throw new RuntimeException("Scope not present!");
    }
    Scope scope = optional.get();

    String variableName = strings[1]
        .substring(strings[1].indexOf("{") + 1, strings[1].indexOf("}"));

    boolean isCommand = strings[2].startsWith("$(") || strings[2].startsWith("${");
    String replacement;

    Keyword key = scope.getScopeVariables().getInitializedVariables().get(variableName);
    if (key == null) {
      output.setReturnCode(-1);
      output.setCommandErrorOutput(Optional
          .of(Stream.of("Variable with name " + variableName + " was not initialized yet!")));
      return output;
    }
    replacement = findValue(strings[2], isCommand, scope);

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
    }

    return output;
  }

//  private String findValue(String string, boolean isCommand, Scope scope) {
//    string =
//        string.startsWith("$(") ? string.substring(string.indexOf("(") + 1, string.indexOf(")"))
//            : string;
//    return isCommand ? findAndExecuteCommand(
//        string, scope) : string;
//  }
//
//  private String findAndExecuteCommand(String string, Scope scope) {
//    CommandIO out = ((ScopeImpl) scope).getExecutor().execute(string, scope);
//    String str = "";
//    if (out.getReturnCode() == 0) {
//
//      Optional<String> reduce = out.getCommandOutput().get().collect(Collectors.toList()).stream()
//          .reduce(String::concat);
//      if (reduce.isPresent()) {
//        return reduce.get();
//      }
//    }
//    return str;
//  }

  private void changeInt(String variableName, String string, Scope scope) {
    Integer i = 0;
    i = Integer.parseInt(string);
    scope.getScopeVariables().getIntegerMap().put(variableName, i);
  }

  private void changeDouble(String variableName, String string, Scope scope) {
    Double d = 0D;
    d = Double.parseDouble(string);
    scope.getScopeVariables().getDoubleMap().put(variableName, d);
  }

  private void changeString(String variableName, String string, Scope scope) {
    scope.getScopeVariables().getStringMap().put(variableName, string);
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
