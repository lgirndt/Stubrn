package stubrn.stubs.handling;

/**
 *
 */
public class ReturnValueCallback<R> implements Callback<R> {

    private final R value;

    public ReturnValueCallback(R value) {
        this.value = value;
    }

    @Override
    public R call(Object[] args) {
        return value;
    }
}
