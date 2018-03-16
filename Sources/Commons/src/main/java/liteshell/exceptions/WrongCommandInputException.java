package liteshell.exceptions;

/**
 * @author xvraniak@stuba.sk
 */

public class WrongCommandInputException extends RuntimeException {

    public WrongCommandInputException(String message) {
        super(message);
    }

    public WrongCommandInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
