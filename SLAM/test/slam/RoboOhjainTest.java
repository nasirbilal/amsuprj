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

        JsimBTYhteys bt = new JsimBTYhteys();
        RoboOhjain instance = new RoboOhjain(bt, 0, 0, 800);
        instance.asetaTestausPaketti(paketti);
        
        Point2D.Double[] expResult = {new Point2D.Double(-707.0,   0.0),
                                      new Point2D.Double(-170.0,  70.4),
                                      new Point2D.Double(-120.2, 120.2),
                                      new Point2D.Double(- 70.4, 170.0),
                                      new Point2D.Double(   0.0, 184.0),
                                      new Point2D.Double(  54.0, 130.3),
                                      new Point2D.Double(  91.9,  91.9),
                                      new Point2D.Double( 130.3,  54.0),
                                      new Point2D.Double( 707.0, 0.0)};
        Point2D.Double[] result = instance.haeEtaisyydet();
        for(int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i].x, result[i].x, 0.05);
            assertEquals(expResult[i].y, result[i].y, 0.05);            
        }
    }

    /**
     * Test of kokeileHakeaUusiMittauspiste method, of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste() {
        System.out.println("kokeileHakeaUusiMittauspiste");
        Point2D.Float nykySijainti = new Point2D.Float(0,0);
        float kulma = 0.0F;
        int[] etaisyydet = new int[37];
        for (int i = 0; i < etaisyydet.length; i++){
            etaisyydet[i] = 800;
        }
        etaisyydet[0] = 500;
        etaisyydet[36] = 500;
        etaisyydet[18] = 500;
        etaisyydet[17] = 500;
        
        RoboOhjain instance = new RoboOhjain(null,0,0,800);
        Point2D.Float expResult = new Point2D.Float(368.6386684F,337.7951038F); //laskimella väännetty
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
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
