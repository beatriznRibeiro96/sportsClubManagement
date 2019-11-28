package exceptions;

public class MyConstraintViolationException extends Exception {
    public MyConstraintViolationException() {
    }

    public MyConstraintViolationException(String message) {
        super(message);
    }

    public MyConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyConstraintViolationException(Throwable cause) {
        super(cause);
    }

    public MyConstraintViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
