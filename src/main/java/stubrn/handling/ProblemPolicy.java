package stubrn.handling;

/**
 *
 */
public interface ProblemPolicy {

    void handleProblem(String msg,Object o);
    void handleProblem(String msg);
}