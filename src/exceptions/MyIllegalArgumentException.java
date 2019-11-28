package exceptions;

public class MyIllegalArgumentException extends Exception {
    public MyIllegalArgumentException() {
    }

    public MyIllegalArgumentException(String message) {
        super(message);
    }

    public MyIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public MyIllegalArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
