package by.kooks.stockExchange.exception;

public class WrongDataException extends Exception {
    public WrongDataException(String message) {
        super(message);
    }
    public WrongDataException(String message, Throwable cause) {
        super(message, cause);
    }
    public WrongDataException(Throwable cause) {
        super(cause);
    }
}
