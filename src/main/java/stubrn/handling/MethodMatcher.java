package stubrn.handling;

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
     * @return a MethodCallback which is able handle a call of the given method
     */
    <R> MethodCallback<R> matchMethod(Method method, ProblemPolicy policy);
}
