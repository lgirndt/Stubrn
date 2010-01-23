package stubrn.stubs.handling;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import stubrn.stubs.annotations.ByName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 */
public class ExistingMethodMatcher implements MethodMatcher {

    private Map<Signature, Callback> signatureCallbacks;
    private Map<String, Callback> nameCallbacks;

    private Map<Class<?>,Object> dummyPrimitiveValues;

    public ExistingMethodMatcher(Object holder){
        fillCallbackMaps(holder);
        fillDummyPrimitiveValueMap();
    }

    private void fillCallbackMaps(Object holder) {
        signatureCallbacks = Maps.newHashMap();
        nameCallbacks = Maps.newHashMap();

        for (Method method : holder.getClass().getDeclaredMethods()) {

            if(Visibilities.isNotAccessible(method)){
                continue;
            }
            method.setAccessible(true);

            if (!method.isAnnotationPresent(ByName.class)) {

                Signature signature = Signature.create(method);
                signatureCallbacks.put(signature, createCallback(holder, method,false));

            } else {
                String name = method.getName();
                if (!nameCallbacks.containsKey(name)) {
                    nameCallbacks.put(name, createCallback(holder, method,true));
                }
            }
        }
    }

    private void fillDummyPrimitiveValueMap(){
        dummyPrimitiveValues = Maps.newHashMap();

        Integer zero = 0;
        putPrimVal(byte.class,zero.byteValue() );
        putPrimVal(char.class,'0');
        putPrimVal(short.class, zero.shortValue());
        putPrimVal(int.class,zero);
        putPrimVal(long.class,zero.longValue());
        putPrimVal(double.class,zero.doubleValue());
        putPrimVal(float.class,zero.floatValue());
    }

    private void putPrimVal(Class<?> type,Object val){
        dummyPrimitiveValues.put(type,val);
    }

    private Object [] createDummyArgs(final Method method){
        ArrayList<Object> dummyArgsList = Lists.newArrayList();
        for(Class<?> t : method.getParameterTypes() ){
            dummyArgsList.add(getDummyValue(t));
        }
        return dummyArgsList.toArray();
    }

    /**
     * Returns a representation of 0 for all primitive types otherwise null
     *
     * @param forType
     * @return
     */
    private Object getDummyValue(Class<?> forType){
        return dummyPrimitiveValues.get(forType);
    }

    private Callback createCallback(final Object holder, final Method method,final boolean ignoreParams) {
        return new Callback() {

            private Object [] defaultArgs;

            private Object [] getDefaultArgs(){
                if(defaultArgs == null){
                    defaultArgs = createDummyArgs(method);
                }
                return defaultArgs;
            }

            @Override
            public Object call(Object[] args) {

                try {
                    args = ignoreParams ? getDefaultArgs() : args;
                    return method.invoke(holder, args);
                } catch (IllegalAccessException e) {
                    throw new StubrnHandlingException(e);
                } catch (InvocationTargetException e) {
                    throw new StubrnHandlingException(e);
                }
            }
        };
    }

    @Override
    public <R> Callback<R> matchMethod(Method method, ProblemPolicy policy) {
        final Callback signatureCallback = signatureCallbacks.get(Signature.create(method));
        if(signatureCallback != null){
            return signatureCallback;
        }
        return nameCallbacks.get(method.getName());
    }
}
