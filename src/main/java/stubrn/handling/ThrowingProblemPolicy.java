package stubrn.handling;

/**
 * A Problem Policy which will throw Exceptions
 */
public class ThrowingProblemPolicy implements ProblemPolicy {
    @Override
    public void handleProblem(String msg, Object o) {
        throw new StubrnHandlingException(msg + ": " + o.toString());                
    }

    @Override
    public void handleProblem(String msg) {
        throw new StubrnHandlingException(msg);
    }
}
