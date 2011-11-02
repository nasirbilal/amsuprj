/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D.Float;
import java.awt.geom.Point2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Olli
 */
public class JsimRoboNäkymäTest {
    
    public JsimRoboNäkymäTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getNäköviiva method, of class JsimRoboNäkymä.
     */
    @Test
    public void testGetNäköviiva() {
        System.out.println("getN\u00e4k\u00f6viiva");
        int i = 0;
        JsimRoboNakyma instance = null;
        Float expResult = null;
        Float result = instance.getNakoviiva(i);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNäkötaulu method, of class JsimRoboNäkymä.
     */
    @Test
    public void testGetNäkötaulu() {
        System.out.println("getN\u00e4k\u00f6taulu");
        JsimRoboNakyma instance = null;
        Float[] expResult = null;
        Float[] result = instance.getNakotaulu();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of leikkaako method, of class JsimRoboNäkymä.
     */
    @Test
    public void testLeikkaako() {
        System.out.println("leikkaako");
        Float näköviiva = null;
        Float karttaviiva = null;
        JsimRoboNakyma instance = null;
        Point2D.Float expResult = null;
        Point2D.Float result = instance.leikkaako(näköviiva, karttaviiva);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
