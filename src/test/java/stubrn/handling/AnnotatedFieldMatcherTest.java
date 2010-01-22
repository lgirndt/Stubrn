package stubrn.handling;

import org.testng.annotations.Test;
import stubrn.annotations.AllByName;

import java.lang.reflect.Method;

import static org.testng.Assert.*;


/**
 *
 */
public class AnnotatedFieldMatcherTest {

    private static class WithStringField {
        @AllByName public String aString = "Foo";
    }

    private static interface WithStringMethod {
        String aString();
    }

    @Test
    public void testSimpleMatchMethod() throws Exception {
        assertReturnsString(callOnMatcher(new WithStringField()), "Foo");
    }


    private static class WithDefaultVisibleStringField {
        @AllByName String aString = "Foo";
    }

    @Test
    public void testDefaultVisibilityOnFields() throws Exception {
        assertReturnsString(callOnMatcher(new WithDefaultVisibleStringField()),"Foo");
    }

    @Test
    public void testProtectedVisibilityOnFields() throws Exception {
        MethodCallback<String> callback = callOnMatcher(new Object(){
            @AllByName protected String aString = "Foo";
        });
        assertNull(callback);
    }

    @Test
    public void testPrivateVisibilityOnFields() throws Exception {
        assertNull( callOnMatcher(new Object(){
             @AllByName private String aString = "Foo";
        }) );
    }

    private void assertReturnsString(MethodCallback<String> callback, String expected) {
        assertNotNull(callback);
        String value = callback.call(null);
        assertEquals(value, expected);
    }

    private MethodCallback<String> callOnMatcher(Object holder) {
        AnnotatedFieldMatcher matcher = new AnnotatedFieldMatcher(holder);
        Method method = getTestMethod();
        MethodCallback<String> callback = matcher.matchMethod(method,new ThrowingProblemPolicy());
        return callback;
    }

    private Method getTestMethod() {
        return WithStringMethod.class.getMethods()[0];
    }

}
