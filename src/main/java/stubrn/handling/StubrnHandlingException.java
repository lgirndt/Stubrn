package stubrn.handling;

/**
 *
 */
public class StubrnHandlingException extends RuntimeException {

    public StubrnHandlingException() {
        super();
    }

    public StubrnHandlingException(String s) {
        super(s);
    }

    public StubrnHandlingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public StubrnHandlingException(Throwable throwable) {
        super(throwable);
    }
}
