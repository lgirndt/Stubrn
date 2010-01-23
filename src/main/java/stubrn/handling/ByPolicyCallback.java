package stubrn.handling;

/**
 *
 */
public class ByPolicyCallback implements Callback {

    private final ProblemPolicy policy;

    public ByPolicyCallback(ProblemPolicy policy) {
        this.policy = policy;
    }

    @Override
    public Object call(Object[] args) {
        policy.handleProblem("Such a method does not exist in stub.");
        return null;
    }
}
