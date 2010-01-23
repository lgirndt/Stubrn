package stubrn.stubs.handling;

/**
 *
 */
public class ReturnValueCallback implements Callback {

    private final Object value;

    public ReturnValueCallback(Object value) {
        this.value = value;
    }

    @Override
    public Object call(Object[] args) {
        return value;
    }
}
