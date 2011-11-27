/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Point2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author L
 */
public class JsimBTYhteysTest {
    
    public JsimBTYhteysTest() {
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
     * Test of testaalahetaJaVastaanota method, of class JsimBTYhteys.
     */
    @Test
    public void testLahetaJaVastaanota() {
        System.out.println("lahetaJaVastaanota");
        BTPaketti paketti = new BTPaketti(0);
        paketti.setNykySijainti(new Point2D.Float(100, 100));
        paketti.setUusiSijainti(new Point2D.Float(500, 1500));
        paketti.setMittausSuunta(new Point2D.Float(500+100, 1500-100)); // Katse kaakkoon.

        JsimBTYhteys instance = new JsimBTYhteys();
        
        int[] etaisyydet = {707, 778, 800, 196, 188, 181, 176, 173, 171, 170,
                            171, 173, 176, 181, 188, 196, 208, 202, 184, 170,
                            159, 150, 143, 138, 135, 132, 130, 130, 130, 132,
                            135, 138, 800, 800, 800, 778, 707};
        BTPaketti expResult = new BTPaketti(1);
        expResult.setNykySijainti(paketti.getUusiSijainti());
        expResult.setEtaisyydet(etaisyydet);

        BTPaketti result = instance.lahetaJaVastaanota(paketti, 0);
        assertEquals(expResult.getNykySijainti().x, result.getNykySijainti().x, 0.0);
        assertEquals(expResult.getNykySijainti().y, result.getNykySijainti().y, 0.0);
        for (int i = 0; i < result.getEtaisyydet().length; ++i)
            assertEquals(expResult.getEtaisyydet()[i], result.getEtaisyydet()[i]);
    }
}
