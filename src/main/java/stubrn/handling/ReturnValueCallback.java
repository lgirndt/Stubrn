package stubrn.handling;

/**
 *
 */
public class ReturnValueCallback<R> implements MethodCallback<R>{

    private final R value;

    public ReturnValueCallback(R value) {
        this.value = value;
    }

    @Override
    public R call(Object[] args) {
        return value;
    }
}
