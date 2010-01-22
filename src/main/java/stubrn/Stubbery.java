package stubrn;

import com.google.common.collect.Lists;
import stubrn.handling.*;

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
        // TODO
        this.defaultCallback = null;
        this.problemPolicy = new ThrowingProblemPolicy();

    }

    public Stubbery(){
        this(Stubbery.class.getClassLoader());
    }

    private <T> InvocationHandler createInvocationHandler(Object holder,Class<T> forClass){

        Collection<MethodMatcher> matchers = Lists.<MethodMatcher>newArrayList(
                new AnnotatedFieldMatcher(holder)
        );

        MethodCallbackDispatcher dispatcher =
                new MethodCallbackDispatcher(
                        forClass,holder,defaultCallback,problemPolicy,matchers);
        return new stubrn.handling.InvocationHandler(dispatcher);
    }

    public <T> T stubFor(Class<T> forClass, Object o){

        T proxy = (T) Proxy.newProxyInstance(
                classLoader,
                new Class[] {forClass},createInvocationHandler(o,forClass));
        return proxy;
    }
}
