import org.akhikhl.gretty.VirtualDirContextEx
import org.junit.Assert
import org.junit.Test
/**
 * @author sala
 */
class VirtualDirContextExTest {
  @Test
  public void testVirtualDirContextEx() {
    def virtualDirContextEx = new VirtualDirContextEx()
    virtualDirContextEx.setWebInfJars([new File('c:\\Users\\sala\\.m2\\repository\\jstl\\jstl\\1.2\\jstl-1.2.jar')])
    virtualDirContextEx.allocate()

    def bindings = virtualDirContextEx.listBindings("/WEB-INF/lib")
    Assert.assertFalse(Collections.list(bindings).empty)
  }
}
