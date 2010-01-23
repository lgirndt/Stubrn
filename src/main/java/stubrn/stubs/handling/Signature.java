package stubrn.stubs.handling;

import com.google.common.collect.Lists;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Signature {

    private final Class<?> returnType;
    private final String name;
    private final List<Class<?>> parameterTypes;

    public Signature(Class<?> returnType, String name, List<Class<?>> parameterTypes) {
        this.returnType = returnType;
        this.name = name;
        this.parameterTypes = Collections.unmodifiableList(Lists.newArrayList(parameterTypes));
    }

    public Signature(Class<?> returnType, String name, Class<?> ... parameterTypes) {
        this(returnType,name, Lists.newArrayList(parameterTypes));
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signature signature = (Signature) o;

        if (name != null ? !name.equals(signature.name) : signature.name != null) return false;
        if (parameterTypes != null ? !parameterTypes.equals(signature.parameterTypes) : signature.parameterTypes != null)
            return false;
        if (returnType != null ? !returnType.equals(signature.returnType) : signature.returnType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = returnType != null ? returnType.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parameterTypes != null ? parameterTypes.hashCode() : 0);
        return result;
    }

    public static Signature create(Method method){
        return new Signature(method.getReturnType(),method.getName(),method.getParameterTypes());
    }
}
