package liteshell.exceptions;

/**
 * @author xvraniak@stuba.sk
 */

public class MethodMissingEception extends Exception {

  public MethodMissingEception(String message) {
    super(message);
  }

  public MethodMissingEception(String message, Throwable cause) {
    super(message, cause);
  }
}
