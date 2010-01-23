package stubrn;

import org.testng.annotations.Test;
import stubrn.annotations.ByName;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 *
 */
public class StubberyTest {

    interface I {
        String getString();

        int getIntValue();
    }

    @Test
    public void testStubCreation() {

        Stubbery stubbery = new Stubbery();

        I i = stubbery.stubFor(I.class, new Object() {
            @ByName String getString = "foo";
        });

        assertNotNull(i);
        assertEquals(i.getString(), "foo");
    }

    @Test
    public void testComplexStub() {
        Stubbery stubbery = new Stubbery();
        I i = stubbery.stubFor(I.class, new Object() {
            @ByName
            String getString = "foo";

            public int getIntValue() {
                return 42;
            }
        });
        assertNotNull(i);
        assertEquals(i.getString(), "foo");
        assertEquals(i.getIntValue(), 42);
    }

    interface CorrectOrder {
        int method();
        int method(int a);
        int method(String a);
        int method(double a);
    }

    @Test
    public void testPrecedencePickByNameFieldsFirst(){

        Stubbery stubbery = new Stubbery();

        Object holder = new Object(){
            @ByName int method = 1;
            int method() {
                return 2;
            }
        };

        CorrectOrder stub = stubbery.stubFor(CorrectOrder.class,holder);
        assertEquals(1,stub.method());
        assertEquals(1,stub.method(42));
        assertEquals(1,stub.method("foo"));
        assertEquals(1,stub.method(23.42));
    }
}
