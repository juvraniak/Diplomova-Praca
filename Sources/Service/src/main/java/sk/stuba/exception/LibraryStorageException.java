package sk.stuba.exception;

public class LibraryStorageException extends RuntimeException {

    public LibraryStorageException(String message) {
        super(message);
    }

    public LibraryStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
