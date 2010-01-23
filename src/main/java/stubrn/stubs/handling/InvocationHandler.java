package stubrn.stubs.handling;

import java.lang.reflect.Method;

/**
 *
 */
public class InvocationHandler implements java.lang.reflect.InvocationHandler {

    private final MethodCallbackDispatcher dispatcher;

    public InvocationHandler(MethodCallbackDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Callback callback = dispatcher.determineCallback(method);
        return callback.call(objects);
    }
}
