package exceptions;

public class MyNoRecipientException extends Exception {
    public MyNoRecipientException() {
    }

    public MyNoRecipientException(String message) {
        super(message);
    }

    public MyNoRecipientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyNoRecipientException(Throwable cause) {
        super(cause);
    }

    public MyNoRecipientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
