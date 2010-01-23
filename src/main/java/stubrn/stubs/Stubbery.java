package stubrn.stubs;

import com.google.common.collect.Lists;
import stubrn.stubs.handling.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 *
 */
public class Stubbery {

    private final ClassLoader classLoader;

    private Callback defaultCallback;

    private ProblemPolicy problemPolicy;

    public Stubbery(ClassLoader classLoader){
        this.classLoader = classLoader;
        this.problemPolicy = new ThrowingProblemPolicy();
        this.defaultCallback = new ByPolicyCallback(problemPolicy);
    }

    public Stubbery(){
        this(Stubbery.class.getClassLoader());
    }

    private <T> InvocationHandler createInvocationHandler(Object holder,Class<T> forClass){

        Collection<MethodMatcher> matchers = Lists.<MethodMatcher>newArrayList(
                new AnnotatedFieldMatcher(holder),
                new ExistingMethodMatcher(holder)
        );

        MethodCallbackDispatcher dispatcher =
                new MethodCallbackDispatcher(
                        forClass,holder,defaultCallback,problemPolicy,matchers);
        return new stubrn.stubs.handling.InvocationHandler(dispatcher);
    }

    public <T> T stubFor(Class<T> forClass, Object o){

        T proxy = (T) Proxy.newProxyInstance(
                classLoader,
                new Class[] {forClass},createInvocationHandler(o,forClass));
        return proxy;
    }
}