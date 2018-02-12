package ponychan;

public class ChanThreadMatchException extends Exception {
    public ChanThreadMatchException() {
        super();
    }

    public ChanThreadMatchException(String message) {
        super(message);
    }

    public ChanThreadMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChanThreadMatchException(Throwable cause) {
        super(cause);
    }
}
