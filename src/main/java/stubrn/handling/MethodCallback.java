package stubrn.handling;

/**
 * Executes an arbitrary method call
 */
public interface MethodCallback<R> {

    R call(Object [] args);    
}
