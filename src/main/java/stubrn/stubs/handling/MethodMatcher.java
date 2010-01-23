package stubrn.stubs.handling;

import java.lang.reflect.Method;

/**
 *
 */
public interface MethodMatcher {

    /**
     *
     *
     * @param method
     * @param policy
     * @return a Callback which is able handle a call of the given method
     */
    <R> Callback<R> matchMethod(Method method, ProblemPolicy policy);
}
