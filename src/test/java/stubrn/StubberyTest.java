package stubrn;

import org.testng.Assert;
import org.testng.annotations.Test;
import stubrn.annotations.AllByName;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 *
 */
public class StubberyTest {

    @Test
    public void testStubCreation(){

        Stubbery stubbery = new Stubbery();

        ExampleInterface intf = stubbery.stubFor(ExampleInterface.class,new Object(){
            @AllByName String getString = "foo";
            @AllByName List<String> getStringList(){ return newArrayList();};
        });

        Assert.assertNotNull(intf);
    }
}
