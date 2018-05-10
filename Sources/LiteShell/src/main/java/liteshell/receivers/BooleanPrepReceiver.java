package liteshell.receivers;

import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;
import liteshell.utils.StringUtils;

/**
 * @author xvraniak@stuba.sk
 */

public class BooleanPrepReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings, Optional<Scope> scope) {
//    String[] andOrSplit = StringUtils.removeEmptyStrings(strings[1].split("[||]|&&"));
    String[] andOrSplit = prepareInput(strings, scope.get());

    String result = "";
    for (int i = 0; i < andOrSplit.length; i = i + 2) {
      String current = andOrSplit[i];

      if (i != 0) {
        String sign = andOrSplit[i - 1];
        result = evaluateWholeExpression(current, result, sign);
      } else {
        result = current;
      }
    }


    commandIO.setReturnCode(0);
    commandIO.setCommandOutput(CommandIO.prepareIO(result));
    return commandIO;
  }

  private String evaluateWholeExpression(String current, String result, String sign) {
    switch (sign) {
      case "&&":
        return new Boolean(resolveBoolean(current) && resolveBoolean(result)).toString();
      case "||":
        return new Boolean(resolveBoolean(current) || resolveBoolean(result)).toString();
      default:
        return "false";
    }
  }

  private String prepareBooleanExpression(String expression, boolean isCommand,
      boolean isInisInitializedVariable, Scope scope) {
    if (isCommand) {
      expression = expression.substring(expression.indexOf("(") + 1, expression.length() - 1);
    }
    String regexp = ">|>=|==|!=|<=|<";
    String[] variables = StringUtils.removeEmptyStrings(expression.split(regexp));
    isCommand = variables[0].startsWith("$(");
    isInisInitializedVariable = variables[0].startsWith("${");
    String val1 = findValue(variables[0], isCommand, isInisInitializedVariable, scope);
    if (variables.length > 1) {
      String sign = expression
          .substring(
              expression.indexOf(expression.charAt(variables[0].length() - 1)) + 1,
              expression.indexOf(variables[1],
                  expression.indexOf(expression.charAt(variables[0].length() - 1)) + 1));
      isCommand = variables[1].startsWith("$(");
      isInisInitializedVariable = variables[1].startsWith("${");
      String val2 = findValue(variables[1], isCommand, isInisInitializedVariable, scope);

      switch (sign) {
        case "==":
          if (val1.equals(val2)) {
            return "true";
          } else {
            return "false";
          }
        case "!=":
          if (!val1.equals(val2)) {
            return "true";
          } else {
            return "false";
          }
        case "<=":
          if (val1.compareTo(val2) <= 0) {
            return "true";
          } else {
            return "false";
          }
        case ">=":
          if (val1.compareTo(val2) >= 0) {
            return "true";
          } else {
            return "false";
          }
        case "<":
          if (val1.compareTo(val2) < 0) {
            return "true";
          } else {
            return "false";
          }
        case ">":
          if (val1.compareTo(val2) > 0) {
            return "true";
          } else {
            return "false";
          }
      }
    } else {
      return val1;
    }
    return null;
  }

  private String[] prepareInput(String[] strings, Scope scope) {
    String[] variables = StringUtils.removeEmptyStrings(strings[1].split("[||]|&&"));
    int size = variables.length;
    int newSize = size * 2 - 1;
    String[] expression = new String[newSize];
    int firstIndex = 0;
    int secondIndex = 0;
    for (int i = 0, j = 0; i < newSize; i = i + 2, j++) {
      boolean isCommand = variables[j].startsWith("$(");
      boolean isInitializedVariable = variables[j].startsWith("${");

      if (strings[1].indexOf(variables[j].charAt(variables[j].length() - 1), firstIndex) + 1
          < strings[1].length() - variables[variables.length - 1].length() - 1) {
        String sign = strings[1]
            .substring(
                strings[1].indexOf(variables[j].charAt(variables[j].length() - 1), firstIndex) + 1,
                strings[1].indexOf(variables[j + 1], secondIndex));
        firstIndex =
            strings[1].indexOf(variables[j].charAt(variables[j].length() - 1), firstIndex) + 1;
        secondIndex = strings[1].indexOf(variables[j + 1], secondIndex);
        expression[i + 1] = sign;
      } else if (strings[1].indexOf(variables[j].charAt(variables[j].length() - 1), firstIndex) + 1
          == strings[1].length() - variables[variables.length - 1].length() - 1) {
        expression[i + 1] = strings[1]
            .substring(
                strings[1].indexOf(variables[j].charAt(variables[j].length() - 1), firstIndex) + 1,
                strings[1].indexOf(variables[j].charAt(variables[j].length() - 1), firstIndex) + 2);
        firstIndex += 2;
      }
//      String replacement = findValue(variables[j], isCommand, isInitializedVariable,
//          scope);
      expression[i] = prepareBooleanExpression(variables[j], isCommand,
          isInitializedVariable, scope);
    }
    return expression;
  }

  private Boolean resolveBoolean(String replacement) {
    return replacement.equals("true") ? true : replacement.equals("false") ? false : null;
  }
}
