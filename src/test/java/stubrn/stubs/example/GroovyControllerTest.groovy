package stubrn.stubs.example

import org.testng.Assert
import org.testng.annotations.Test
import stubrn.stubs.Stubbery

/*
*
*/
class GroovyControllerTest {

  @Test
  void simpleMapUsage() {
    Stubbery stubbery = new Stubbery();

    EntityDAO stub = stubbery.stubFor(EntityDAO.class, [findByName:[new Entity("foo")]]);

    Controller sud = new Controller();
    sud.setEntityDAO(stub);

    Assert.assertEquals(sud.listEntities("foo").size(), 1);
  }

  /** TODO: Unclear, how this should work...
  @Test
  void withGroovysStubFor() {
    def daoStub = new MockFor(EntityDAOImpl.class)
    daoStub.demand.findByName { str -> return [new Entity("foo")] }


    daoStub.use {
      Controller sud = new Controller();
      sud.setEntityDAO(new EntityDAOImpl());

      Assert.assertEquals(sud.listEntities("foo").size(), 1);      
    }
  }
   */
}
