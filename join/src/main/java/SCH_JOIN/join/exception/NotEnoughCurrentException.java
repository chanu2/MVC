package SCH_JOIN.join.exception;

public class NotEnoughCurrentException extends RuntimeException{

    public NotEnoughCurrentException() {
        super();
    }

    public NotEnoughCurrentException(String message) {
        super(message);
    }

    public NotEnoughCurrentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughCurrentException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughCurrentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
