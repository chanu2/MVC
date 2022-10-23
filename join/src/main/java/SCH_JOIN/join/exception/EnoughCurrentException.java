package SCH_JOIN.join.exception;

public class EnoughCurrentException extends RuntimeException{
    public EnoughCurrentException() {
        super();
    }

    public EnoughCurrentException(String message) {
        super(message);
    }

    public EnoughCurrentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnoughCurrentException(Throwable cause) {
        super(cause);
    }

    protected EnoughCurrentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
