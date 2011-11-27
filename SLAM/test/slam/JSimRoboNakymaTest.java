/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lauri
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
        final Point2D.Float p = new Point2D.Float(-101, -97);
        final float a = 135; // Katse 135 asteen kulmassa.
        final JsimRoboNakyma instance = new JsimRoboNakyma(p, a, 5, 800);

        Line2D.Float[] result = instance.getNakotaulu();
        Line2D.Float[] expResult = {new Line2D.Float(p.x, p.y, -666.7f, -662.7f),
                                    new Line2D.Float(p.x, p.y, -901.0f, - 97.0f),
                                    new Line2D.Float(p.x, p.y, -666.7f,  468.7f),
                                    new Line2D.Float(p.x, p.y, -101.0f,  703.0f),
                                    new Line2D.Float(p.x, p.y,  464.7f,  468.7f)};

        for (int i = 0; i < result.length; ++i) {
            assertEquals(expResult[i].x1, result[i].x1, 0.05f);
            assertEquals(expResult[i].y1, result[i].y1, 0.05f);
            assertEquals(expResult[i].x2, result[i].x2, 0.05f);
            assertEquals(expResult[i].y2, result[i].y2, 0.05f);
        }
    }

    /**
     * Test of laskeLeikkauspiste method, of class JsimRoboNäkymä.
     */
    @Test
    public void testLaskeLeikkauspiste() {
        System.out.println("laskeLeikkauspiste");
        final Point2D.Float p = new Point2D.Float(0, 0);
        final JsimRoboNakyma instance = new JsimRoboNakyma(p, 0, 9, 800);
        final Line2D.Float[] taulu = instance.getNakotaulu();
        float dx = 10*(taulu[7].x2 - taulu[7].x1);
        float dy = 10*(taulu[7].y2 - taulu[7].y1);
        Line2D.Float seina = new Line2D.Float(taulu[7].x1-dx+100,
                                              taulu[7].y1-dy+100,
                                              taulu[7].x2+dx+100,
                                              taulu[7].y2+dy+100);
        Point2D.Float[] expResult = { new Point2D.Float(  0.0f, 341.4f),
                                      new Point2D.Float( 70.7f, 170.7f),
                                      new Point2D.Float(100.0f, 100.0f),
                                      new Point2D.Float(120.7f,  50.0f),
                                      new Point2D.Float(141.4f,   0.0f),
                                      new Point2D.Float(170.7f,- 70.7f),
                                      new Point2D.Float(241.4f,-241.4f),
                                      null,
                                      null};
        for (int i = 0; i < taulu.length; ++i) {
            Point2D.Float result = instance.laskeLeikkauspiste(taulu[i], seina);
            
            assertFalse(result == null && expResult[i] != null);
            assertFalse(result != null && expResult[i] == null);
            if (result != null) {
                assertEquals(expResult[i].x, result.x, 0.05f);
                assertEquals(expResult[i].y, result.y, 0.05f);
            }
        }
    }
}
