package stubrn.stubs

import org.testng.Assert
import org.testng.annotations.Test

/*
 *
 */
class MapsInGroovyTest {

  @Test
  void testStubberyWithGroovyMap(){

    def stubbery = new Stubbery()

    SimpleMethod intf = stubbery.stubFor(SimpleMethod.class, [method:42])
    Assert.assertEquals(intf.method(23),42)    
  }
}
