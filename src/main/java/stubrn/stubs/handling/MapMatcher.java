package stubrn.stubs.handling;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 */
public class MapMatcher implements MethodMatcher {

    private final Map<String,Object> holder;

    public MapMatcher(Map<String, Object> holder) {
        this.holder = holder;
    }

    @Override
    public <R> Callback matchMethod(Method method, ProblemPolicy policy) {

        Object value = holder.get(method.getName());
        if(value == null){
            return null;
        }

        return new ReturnValueCallback(value);
    }
}
