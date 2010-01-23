package stubrn.stubs.handling;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 *
 */
public class SignatureTest {

    interface A {
        String aMethod(int a,Double b,Object [] c);
    }

    interface B {
        String aMethod(int a,Double b,Object [] c);
    }

    @Test
    public void testCreation(){
        Signature s = Signature.create(A.class.getMethods()[0]);
        assertEquals(s.getReturnType(), String.class);
        assertEquals(s.getName(),"aMethod");
        assertEquals(s.getParameterTypes().size(),3);
        assertEquals(s.getParameterTypes().get(0),int.class);
        assertEquals(s.getParameterTypes().get(1),Double.class);
        
    }

    @Test
    public void testEquality(){
        Signature a = Signature.create(A.class.getMethods()[0]);
        Signature b = Signature.create(B.class.getMethods()[0]);
        assertEquals(a,b);
        assertTrue(a.hashCode() == b.hashCode());
    }
}
