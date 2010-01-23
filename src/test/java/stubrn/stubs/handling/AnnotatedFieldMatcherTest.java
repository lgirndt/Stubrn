package stubrn.stubs.handling;

import com.google.common.collect.Maps;
import org.testng.annotations.Test;
import stubrn.stubs.annotations.ByName;

import java.lang.reflect.Method;
import java.util.Map;

import static org.testng.Assert.*;


/**
 *
 */
public class AnnotatedFieldMatcherTest {

    private static class WithStringField {
        @ByName
        public String aString = "Foo";
    }

    private static interface WithStringMethod {
        String aString();
    }

    @Test
    public void testSimpleMatchMethod() throws Exception {
        assertReturnsString(getCallbackFromMatcher(new WithStringField()), "Foo");
    }


    private static class WithDefaultVisibleStringField {
        @ByName
        String aString = "Foo";
    }

    @Test
    public void testDefaultVisibilityOnFields() throws Exception {
        assertReturnsString(getCallbackFromMatcher(new WithDefaultVisibleStringField()),"Foo");
    }

    @Test
    public void testProtectedVisibilityOnFields() throws Exception {
        Callback callback = getCallbackFromMatcher(new Object(){
            @ByName
            protected String aString = "Foo";
        });
        assertNull(callback);
    }

    @Test
    public void testPrivateVisibilityOnFields() throws Exception {
        assertNull( getCallbackFromMatcher(new Object(){
             @ByName
             private String aString = "Foo";
        }) );
    }


    private static interface WithMethods {
        String  aString();
        int     anInt();
        Integer anInteger();
    }

    @Test
    public void testWithSeveralFields() throws Exception {
        Map<String,Method> methods = createNameMethodMap(WithMethods.class.getMethods());

        AnnotatedFieldMatcher matcher = new AnnotatedFieldMatcher(new Object(){
            @ByName String aString = "Foo";
            @ByName int anInt = 42;
            @ByName Integer anInteger = 23;
        });

        Callback aStrCallback =
                matcher.matchMethod(methods.get("aString"),new ThrowingProblemPolicy());
        assertEquals(aStrCallback.call(null),"Foo");

        Callback anIntCallback =
                matcher.matchMethod(methods.get("anInt"), new ThrowingProblemPolicy());
        assertEquals(anIntCallback.call(null), 42);

        Callback anIntegerCallback =
                matcher.matchMethod(methods.get("anInteger"), new ThrowingProblemPolicy());
        assertEquals(anIntegerCallback.call(null),23);
    }

    private static interface WithOverloadedMethods {
        String aString();
        String aString(int a);
        String aString(String a);
    }

    @Test
    public void testWithOverloadedMethods(){
        Method [] methods = WithOverloadedMethods.class.getMethods();
        assertEquals(methods.length,3);

        AnnotatedFieldMatcher matcher = new AnnotatedFieldMatcher(new Object(){
           @ByName
           String aString = "Foo";
        });

        assertMatchingString(matcher, methods[0], "Foo");
        assertMatchingString(matcher, methods[1], "Foo");
        assertMatchingString(matcher, methods[2], "Foo");
    }

    @Test
    public void testUnassignedField(){
        Callback callback = getCallbackFromMatcher(new Object(){
            @ByName String aString;
        });
        assertNotNull(callback);
        assertNull(callback.call(null));
    }

    private void assertMatchingString(AnnotatedFieldMatcher matcher, Method m, String expected) {
        Callback callback = matcher.matchMethod(m, new ThrowingProblemPolicy());
        assertNotNull(callback);
        assertEquals(callback.call(null), expected);
    }

    private Map<String,Method> createNameMethodMap(Method [] methods){
        Map<String,Method> map = Maps.newHashMap();
        for(Method m : methods){
            map.put(m.getName(),m);
        }

        return map;
    }

    private void assertReturnsString(Callback callback, String expected) {
        assertNotNull(callback);
        String value = (String) callback.call(null);
        assertEquals(value, expected);
    }

    private Callback getCallbackFromMatcher(Object holder) {
        AnnotatedFieldMatcher matcher = new AnnotatedFieldMatcher(holder);
        Method method = getTestMethod();
        Callback callback = matcher.matchMethod(method,new ThrowingProblemPolicy());
        return callback;
    }

    private Method getTestMethod() {
        return WithStringMethod.class.getMethods()[0];
    }

}
