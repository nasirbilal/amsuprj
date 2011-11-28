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
public class RoboOhjainTest {
    
    public RoboOhjainTest() {
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
     * Test of haeEtaisyydet method, of class RoboOhjain.
     */
    @Test
    public void testHaeEtaisyydet() {
        System.out.println("haeEtaisyydet");

        BTPaketti paketti = new BTPaketti(0);
        paketti.setNykySijainti(new Point2D.Float(100, 100));
        paketti.setUusiSijainti(new Point2D.Float(500, 1500));
        paketti.setMittausSuunta(new Point2D.Float(500+100, 1500-100)); // Katse kaakkoon.
        int[] etaisyydet = {707, 184, 170, 184, 184, 141, 130, 141, 707};
        paketti.setEtaisyydet(etaisyydet);

        RoboOhjain instance = new RoboOhjain(null, 0, 0, 800);
        instance.asetaTestausPaketti(paketti);
        
        Point2D.Double[] expResult = null;
        Point2D.Double[] result = instance.haeEtaisyydet();
        for(Point2D.Double p : result)
            System.out.println(p == null ? "null" : p.toString());
//        fail("The test case is a prototype.");
    }

    /**
     * Test of kokeileHakeaUusiMittauspiste method, of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste() {
        System.out.println("kokeileHakeaUusiMittauspiste");
        Point2D.Float nykySijainti = null;
        float kulma = 0.0F;
        int[] etaisyydet = null;
        RoboOhjain instance = null;
        Point2D.Float expResult = null;
//        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of kokeileLisataHavainnotKarttaan method, of class RoboOhjain.
     */
    @Test
    public void testKokeileLisataHavainnotKarttaan() {
        System.out.println("kokeileLisataHavainnotKarttaan");
        Point2D.Float nykySijainti = null;
        float kulma = 0.0F;
        int[] etaisyydet = null;
        RoboOhjain instance = null;
//        instance.kokeileLisataHavainnotKarttaan(nykySijainti, kulma, etaisyydet);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
