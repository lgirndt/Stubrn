package stubrn.stubs.example;

import com.google.common.collect.Lists;
import org.testng.Assert;
import org.testng.annotations.Test;
import stubrn.stubs.Stubbery;
import stubrn.stubs.annotations.ByName;

import java.util.List;

/**
 *
 */
public class ControllerTest {

    @Test
    public void simpleUsage(){

        Stubbery stubbery = new Stubbery();

        EntityDAO stub = stubbery.stubFor(EntityDAO.class,new Object(){
            List<Entity> findByName(String name) {
                return Lists.newArrayList(new Entity("foo"),new Entity("foo"));
            }
        });

        Controller sud = new Controller();
        sud.setEntityDAO(stub);

        Assert.assertEquals(sud.listEntities("foo").size(),2);
    }

    @Test
    public void usageWithFields(){

        Stubbery stubbery = new Stubbery();

        EntityDAO stub = stubbery.stubFor(EntityDAO.class,new Object(){
            @ByName List<Entity> findByName = Lists.newArrayList(new Entity("foo"));
        });

        Controller sud = new Controller();
        sud.setEntityDAO(stub);

        Assert.assertEquals(sud.listEntities("foo").size(),1);
    }
}
