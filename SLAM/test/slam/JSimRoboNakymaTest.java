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

    final Point2D.Float p = new Point2D.Float(-101, -97);
    final float a = 31; // Katse 31 asteen kulmassa.
    final private JsimRoboNakyma instance;
    
    public JSimRoboNakymaTest() {
        instance = new JsimRoboNakyma(p, a, 5, 800);
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
        Line2D.Float[] expResult = {new Line2D.Float(p.x, p.y, -513,  589),
                                    new Line2D.Float(p.x, p.y,   93,  679),
                                    new Line2D.Float(p.x, p.y,  585,  315),
                                    new Line2D.Float(p.x, p.y,  675, -291),
                                    new Line2D.Float(p.x, p.y,  311, -783)};
        Line2D.Float[] result = instance.getNakotaulu();
        for (int i = 0; i < result.length; ++i) {
            assertEquals(expResult[i].x1, result[i].x1, 0.5f);
            assertEquals(expResult[i].y1, result[i].y1, 0.5f);
            assertEquals(expResult[i].x2, result[i].x2, 0.5f);
            assertEquals(expResult[i].y2, result[i].y2, 0.5f);
        }
    }

    /**
     * Test of leikkaako method, of class JsimRoboNäkymä.
     */
    @Test
    public void testLeikkaako() {
        System.out.println("leikkaako");
        Float näköviiva = null;
        Float karttaviiva = null;
        Point2D.Float expResult = null;
        Point2D.Float result = instance.laskeLeikkauspiste(näköviiva, karttaviiva);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
