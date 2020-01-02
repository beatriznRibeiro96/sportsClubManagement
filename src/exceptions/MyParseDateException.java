package exceptions;

public class MyParseDateException extends Exception {
    public MyParseDateException() {
    }

    public MyParseDateException(String message) {
        super(message);
    }

    public MyParseDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyParseDateException(Throwable cause) {
        super(cause);
    }

    public MyParseDateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
