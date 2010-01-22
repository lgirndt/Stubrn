package stubrn;

import stubrn.handling.InvocationHandlerDispatcher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 *
 */
public class Stubbery {

    private final ClassLoader classLoader;

    public Stubbery(ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    public Stubbery(){
        this(Stubbery.class.getClassLoader());
    }

    private InvocationHandler createInvocationHandler(Object holder){
        // TODO
        return new InvocationHandlerDispatcher();
    }

    public <T> T stubFor(Class<T> forClass, Object o){

        T proxy = (T) Proxy.newProxyInstance(
                classLoader,
                new Class[] {forClass},createInvocationHandler(o));
        return proxy;
    }
}
