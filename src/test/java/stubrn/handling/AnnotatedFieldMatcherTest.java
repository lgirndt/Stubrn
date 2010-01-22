package stubrn.handling;

import org.testng.annotations.Test;
import stubrn.annotations.AllByName;

import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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
        Method method = WithStringMethod.class.getMethods()[0];
        WithStringField withField = new WithStringField();
        AnnotatedFieldMatcher matcher = new AnnotatedFieldMatcher(withField);

        MethodCallback<String> callback = matcher.matchMethod(method,new ThrowingProblemPolicy());
        assertNotNull(callback);
        String value = callback.call(null);
        assertEquals(value,"Foo");
    }
}
