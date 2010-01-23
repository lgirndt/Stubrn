package stubrn.stubs.handling;

/**
 * Executes an arbitrary method call
 */
public interface Callback<R> {

    R call(Object [] args);    
}
