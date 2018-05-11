package liteshell.receivers;

import java.math.BigDecimal;
import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ArithmeticReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO commandIO, String[] strings, Optional<Scope> scope) {
    try {
      String[] expression = prepareInput(strings, scope.get());

      BigDecimal total = BigDecimal.ZERO;

      for (int i = 0; i < expression.length; i = i + 2) {
        BigDecimal d = new BigDecimal(expression[i]);
        if (i != 0) {
          String sign = expression[i - 1];
          total = compute(d, total, sign);
        } else {
          total = total.add(d);
        }
      }

      commandIO.setReturnCode(0);
      commandIO.setCommandOutput(CommandIO.prepareIO(new Double(total.doubleValue()).toString()));
    } catch (Exception e) {
      commandIO.setReturnCode(-1);
      commandIO.setCommandOutput(CommandIO.prepareIO(e.getMessage()));
    }

    return commandIO;
  }

  private BigDecimal compute(BigDecimal d, BigDecimal total, String sign) {
    switch (sign) {
      case "+":
        return total.add(d);
      case "-":
        return total.subtract(d);
      case "*":
        return total.multiply(d);
      case "/":
        return total.divide(d);
      case "%":
        return total.remainder(d);
      default:
        return total;
    }
  }

  private String[] prepareInput(String[] strings, Scope scope) {
    String[] variables = strings[1].split("-|[*|/|+|%]");
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
      String replacement = findValue(variables[j], isCommand, isInitializedVariable,
          scope);
      expression[i] = replacement;
    }
    return expression;
  }
}
