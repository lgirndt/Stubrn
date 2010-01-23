package stubrn.stubs.handling;

import org.testng.annotations.Test;
import stubrn.stubs.annotations.ByName;

import java.lang.reflect.Method;

import static org.testng.Assert.*;

/**
 *
 */
public class ExistingMethodMatcherTest {

    interface SingleMethod {
        int foo(String a,Double b);
    }

    @Test
    public void testMatchBySignatureWithPublicVisibility(){

        Callback callback = createCallback(getSingleMethod(), new Object(){
            public int foo(String a,Double b){
                return 42;
            }
        });

        assertCallbackReturns(callback, 42);
    }

    @Test
    public void testMatchBySignatureWithDefaultVisibility(){
        Callback c = createCallback(getSingleMethod(),new Object(){
            int foo(String a,Double b){
                return 42;
            }
        });
        assertCallbackReturns(c,42);
    }

    @Test
    public void testMatchBySignatureWithProtectedVisibility(){
        Callback c = createCallback(getSingleMethod(),new Object(){
            protected int foo(String a,Double b){
                return 42;
            }
        });
        assertNull(c);
    }

    @Test
    public void testMatchBySignatureWithPrivateVisibility(){
        Callback c = createCallback(getSingleMethod(), new Object(){
            private int foo(String a,Double b){
                return 42;
            }
        });
        assertNull(c);
    }

    @Test    
    public void testMatchByNameWithPublicVisibility(){
        Callback c = createCallback(getSingleMethod(), new Object(){
            public @ByName int foo(){
                return 42;
            }
        });
        assertCallbackReturns(c,42);
    }

    @Test
    public void testMatchByNameWithDefaultVisibility(){
        Callback c = createCallback(getSingleMethod(), new Object(){
           @ByName int foo(){
               return 42;
           }
        });
        assertCallbackReturns(c,42);
    }

    @Test
    public void testMatchByNameWithProtectedVisibility(){
        Callback c = createCallback(getSingleMethod(), new Object(){
            protected @ByName int foo(){
                return 42;
            }
        });
        assertNull(c);
    }

    @Test
    public void testMatchByNameWithPrivateVisibility(){
        Callback c = createCallback(getSingleMethod(),new Object(){
            private @ByName int foo(){
                return 42;
            }
        });
        assertNull(c);
    }


    interface AllTypes {
        void method(byte b,char c,short s,int i,double d,float f);
    }

    @Test
    public void testDummyValueSubstitution(){
        Object holder = new Object(){
            @ByName void method(byte b,char c,short s,int i,double d,float f){
                Integer zero = 0;
                assertEquals(b, zero.byteValue());
                assertEquals(c,'0');
                assertEquals(s,zero.shortValue());
                assertEquals(i,zero.intValue());
                assertEquals(d,zero.doubleValue());
                assertEquals(f,zero.floatValue());
            }
        };
        Callback callback = createCallback(AllTypes.class.getMethods()[0],holder);
        callback.call(null);
    }


    interface SignatureNamePrecedence {
        int method(int a);
        int method(int a,int b,int c);
        int method(int a,int b,int c,int d);
    }

    @Test
    public void testSignatureOverNamePrecedence(){
        Object holder = new Object(){
            @ByName int method() {
                return 23;
            }

            int method(int a){
                return 42;
            }
        };
        Method[] methods = SignatureNamePrecedence.class.getMethods();
        Callback callback = createCallback(methods[0],holder);
        assertEquals(callback.call(toArgs(0)),42);

        assertCallbackReturns(createCallback(methods[1],holder),23);
        assertCallbackReturns(createCallback(methods[2],holder),23);
    }

    private void assertCallbackReturns(Callback callback, int expected) {
        assertNotNull(callback,"Callback should not be null");
        assertEquals(callback.call( toArgs(null,null) ),expected);
    }

    private Method getSingleMethod() {
        return SingleMethod.class.getMethods()[0];
    }

    private Callback createCallback(Method method, Object holder) {
        ExistingMethodMatcher matcher = new ExistingMethodMatcher(holder);
        Callback callback = matcher.matchMethod(
                method,new ThrowingProblemPolicy());
        return callback;
    }

    private Object [] toArgs(Object ... objs){
        return objs;
    }

}
