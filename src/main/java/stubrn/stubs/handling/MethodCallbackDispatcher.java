package stubrn.stubs.handling;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class MethodCallbackDispatcher<T> {

    private final Class<T> onType;

    private final Object holder;

    private final ProblemPolicy problemPolicy;

    private final Map<Signature, Callback> signatureCallbacks;

    private final Callback defaultCallback;

    public MethodCallbackDispatcher(
            Class<T>onType,
            Object holder,
            Callback defaultCallback,
            ProblemPolicy problemPolicy,
            Collection<MethodMatcher> methodMatchers) {
        this.onType = onType;
        this.holder = holder;
        this.defaultCallback = defaultCallback;

        this.problemPolicy = problemPolicy;
        this.signatureCallbacks =
                createSignatureCallbacks(holder,methodMatchers);
    }        

    /**
     * Iterates through the given matchers and registers their MethodCallbacks for the respective
     * Methods. The order of the MethodMatchers in the collection determines the priority of
     * which Callback will be taken. If there is already a registered callback for the method, the
     * current matcher is not asked.
     *
     */
    Map<Signature, Callback> createSignatureCallbacks(
            Object holder,Collection<MethodMatcher> matchers){

        Map<Signature, Callback> map = Maps.newHashMap();
        for(MethodMatcher matcher : matchers){
            for(Method method : onType.getMethods()){

                Signature signature = Signature.create(method);

                if(!map.containsKey(signature)){
                    Callback callback = matcher.matchMethod(method,problemPolicy);
                    if(callback != null){
                        map.put(signature,callback);
                    }
                }
            }
        }
        return map;
    }

    /**
     * Gets the callback for the given method. Either a callback is registered, or the default
     * behaviour will be applied
     *
     * @param method
     * @return
     */
    public Callback determineCallback(Method method){
        Signature s = Signature.create(method);
        Callback callback = signatureCallbacks.get(s);
        return (callback != null) ? callback : defaultCallback;
    }
}
