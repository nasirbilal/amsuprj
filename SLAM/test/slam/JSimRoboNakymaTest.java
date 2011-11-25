/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
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
public class JSimRoboNakymaTest {

    public JSimRoboNakymaTest() {
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
     * Test of getNäkötaulu method, of class JsimRoboNäkymä.
     */
    @Test
    public void testGetNäkötaulu() {
        System.out.println("getN\u00e4k\u00f6taulu");
        JsimRoboNakyma instance = new JsimRoboNakyma(
                new Point2D.Float(-101, -97),
                0, 10, 80);
        Line2D.Float[] expResult = null;
        Line2D.Float[] result = instance.getNakotaulu();
        assertEquals(expResult, result);
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
        Point2D.Float result = instance.laskeLeikkauspiste(näköviiva, karttaviiva);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
