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
    }

    @Test
    public void testStubCreation(){

        Stubbery stubbery = new Stubbery();

        I i = stubbery.stubFor(I.class,new Object(){ @ByName String getString = "foo"; });

        assertNotNull(i);
        assertEquals(i.getString(),"foo");
    }
}
