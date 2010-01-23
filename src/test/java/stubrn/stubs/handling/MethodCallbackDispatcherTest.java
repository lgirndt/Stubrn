package stubrn.stubs.handling;

import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;

/**
 *
 */
public class MethodCallbackDispatcherTest {

    interface I {
        String aMethod();
        int anotherMethod();
        void notStubbed();
    }

    @Test
    public void testDetermineCallback() throws Exception {

        final Callback<String> defaultCallback = new ReturnValueCallback("Foo");
        final Callback<String> aCallback = new ReturnValueCallback("Bar");
        final Callback<Integer> anotherCallback = new ReturnValueCallback(42);

        // we should eat our own dogfood!
        MethodMatcher matcherStub =
                new MethodMatcher(){
                    @Override
                    public Callback<?> matchMethod(Method method, ProblemPolicy policy) {
                        String name = method.getName();
                        if(name.equals("aMethod")){
                            return aCallback;
                        }
                        else if(name.equals("anotherMethod")){
                            return anotherCallback;
                        }
                        else {
                            return null;
                        }
                    }
                };

        MethodCallbackDispatcher<I> dispatcher =
                new MethodCallbackDispatcher(
                        I.class,
                        new Object(),
                        defaultCallback,
                        new ThrowingProblemPolicy(),
                        Lists.<MethodMatcher>newArrayList(matcherStub));

        Method[] methods = I.class.getMethods();
        
        assertEquals(dispatcher.determineCallback(methods[0]), aCallback);
        assertEquals(dispatcher.determineCallback(methods[1]), anotherCallback);
        assertEquals(dispatcher.determineCallback(methods[2]), defaultCallback);
    }
}
