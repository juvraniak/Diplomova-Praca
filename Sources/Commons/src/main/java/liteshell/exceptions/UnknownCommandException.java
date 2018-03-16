package liteshell.exceptions;

/**
 * @author xvraniak@stuba.sk
 */

public class UnknownCommandException extends RuntimeException {

    public UnknownCommandException(String message) {
        super(message);
    }

    public UnknownCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
