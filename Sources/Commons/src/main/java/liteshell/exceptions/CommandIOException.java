package liteshell.exceptions;

public class CommandIOException extends RuntimeException {
    public CommandIOException(String message) {
        super(message);
    }

    public CommandIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
