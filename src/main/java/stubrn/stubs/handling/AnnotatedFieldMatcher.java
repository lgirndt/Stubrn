package stubrn.stubs.handling;

import com.google.common.collect.Maps;
import stubrn.stubs.annotations.ByName;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static stubrn.stubs.handling.Visibilities.isNotAccessible;

/**
 * Creates Method Callbacks for fields in the given Object, which are marked
 * with then respective stubrn annoations.
 */
public class AnnotatedFieldMatcher implements MethodMatcher {

    private static final Class<ByName> ANNOTATION = ByName.class;

    private final Object holder;
    private final Map<String,Field> annotatedFields;

    public AnnotatedFieldMatcher(Object holder) {
        this.holder = holder;
        this.annotatedFields = createAnnotatedFieldMapping();
    }

    private Map<String,Field> createAnnotatedFieldMapping(){

        Map<String,Field> map = Maps.newHashMap();
        for(Field f : holder.getClass().getDeclaredFields()){
            if(isNotAccessible(f)){
                continue;
            }
            f.setAccessible(true);

            if(f.isAnnotationPresent(ANNOTATION)){
                map.put(f.getName(),f);
            }
        }
        return map;
    }


    @Override
    public <R> Callback<R> matchMethod(Method method, ProblemPolicy policy) {
        try {
            Field f = annotatedFields.get(method.getName());
            if(f == null) {
                return null;
            }
            Class<?> methodType = method.getReturnType();
            Class<?> fieldType = f.getType();
            if(!methodType.isAssignableFrom(fieldType)){
                policy.handleProblem(constructProblemMsg(method, methodType, fieldType));
                return null;
            }
            return new ReturnValueCallback<R>((R) f.get(holder));
            
        } catch (IllegalAccessException e) {
            policy.handleProblem(e.getMessage());
            return null;
        }
    }

    private String constructProblemMsg(Method method, Class<?> methodType, Class<?> fieldType) {
        return "Method return type '" +
                methodType.getName() +
                "' and Field return type '" +
                fieldType.getName() +
                "' of method '" +
                method.getName() +
                "' differ";
    }
}
