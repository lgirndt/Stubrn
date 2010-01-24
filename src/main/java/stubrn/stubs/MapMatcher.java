package stubrn.stubs;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 */
class MapMatcher implements MethodMatcher {

    private final Map<String,Object> holder;

    public MapMatcher(Map<String, Object> holder) {
        this.holder = holder;
    }

    @Override
    public Callback matchMethod(Method method) {

        Object value = holder.get(method.getName());
        if(value == null){
            return null;
        }

        return new ReturnValueCallback(value,value.getClass());
    }
}
