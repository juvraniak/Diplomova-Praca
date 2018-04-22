package liteshell.exceptions;

/**
 * @author xvraniak@stuba.sk
 */

public class PluginNotSupportedException extends Exception {

  public PluginNotSupportedException(String message) {
    super(message);
  }

  public PluginNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }
}
