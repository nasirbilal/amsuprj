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
        
        // Kyl nää tulee ihan oikein myötäpäiväjärjestyksessä.
        Point2D.Double[] expResult = {new Point2D.Double(-707.0,   0.0),
                                      new Point2D.Double(-170.0,  70.4),
                                      new Point2D.Double(-120.2, 120.2),
                                      new Point2D.Double(- 70.4, 170.0),
                                      new Point2D.Double(   0.0, 184.0),
                                      new Point2D.Double(  54.0, 130.3),
                                      new Point2D.Double(  91.9,  91.9),
                                      new Point2D.Double( 130.3,  54.0),
                                      new Point2D.Double( 707.0,   0.0)};
        Point2D.Double[] result = instance.haeEtaisyydet();

        for(int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i].x, result[i].x, 0.05);
            assertEquals(expResult[i].y, result[i].y, 0.05);            
        }
    }

    /**
     * Test of kokeileHakeaUusiMittauspiste method(normiliike1), of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste1() {
        System.out.println("kokeileHakeaUusiMittauspiste1");
        Point2D.Float nykySijainti = new Point2D.Float(0,0);
        float kulma = 0.0F;
        BTYhteys bt = new JsimBTYhteys();
        int[] etaisyydet = new int[37];
        for (int i = 0; i < etaisyydet.length; i++){
            etaisyydet[i] = 800;
        }
        etaisyydet[0] = 500;
        etaisyydet[36] = 500;
        etaisyydet[18] = 500;
        etaisyydet[17] = 500;
        
        RoboOhjain instance = new RoboOhjain(bt,0,0,800);
        Point2D.Float expResult = new Point2D.Float(368.6386684F,337.7951038F); //laskimella väännetty
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of kokeileHakeaUusiMittauspiste method (normiliike2), of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste2() {
        System.out.println("kokeileHakeaUusiMittauspiste2");
        Point2D.Float nykySijainti = new Point2D.Float(0,0);
        float kulma = 0.0F;
        BTYhteys bt = new JsimBTYhteys();
        int[] etaisyydet = new int[37];
        for (int i = 0; i < etaisyydet.length; i++){
            etaisyydet[i] = 800;
        }
        etaisyydet[0] = 500;
        etaisyydet[36] = 500;
        etaisyydet[18] = 500;
        etaisyydet[19] = 500;
        
        RoboOhjain instance = new RoboOhjain(bt,0,0,800);
        Point2D.Float expResult = new Point2D.Float(-337.7951038F,368.6386684F); //laskimella väännetty
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of kokeileHakeaUusiMittauspiste method (kaikki nähty kaikkialla), of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste3() {
        System.out.println("kokeileHakeaUusiMittauspiste3");
        Point2D.Float nykySijainti = new Point2D.Float(0,0);
        float kulma = 0.0F;
        BTYhteys bt = new JsimBTYhteys();
        int[] etaisyydet = new int[37];
        for (int i = 0; i < etaisyydet.length; i++){
            etaisyydet[i] = 500;
        }
        
        RoboOhjain instance = new RoboOhjain(bt,0,0,800);
        Point2D.Float expResult = new Point2D.Float(0.0F,250.0F); //laskimella väännetty
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of kokeileHakeaUusiMittauspiste method (kaikki nähty kaikkialla*2), of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste4() {
        System.out.println("kokeileHakeaUusiMittauspiste4");
        Point2D.Float nykySijainti = new Point2D.Float(0,0);
        float kulma = 0.0F;
        BTYhteys bt = new JsimBTYhteys();
        int[] etaisyydet = new int[37];
        for (int i = 0; i < etaisyydet.length; i++){
            etaisyydet[i] = 500;
        }
        
        RoboOhjain instance = new RoboOhjain(bt,0,0,800);
        Point2D.Float expResult = new Point2D.Float(0.0F,250.0F); //laskimella väännetty
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of kokeileHakeaUusiMittauspiste method (ei mitään havaittu), of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste5() {
        System.out.println("kokeileHakeaUusiMittauspiste5");
        Point2D.Float nykySijainti = new Point2D.Float(0,0);
        float kulma = 0.0F;
        BTYhteys bt = new JsimBTYhteys();
        int[] etaisyydet = new int[37];
        for (int i = 0; i < etaisyydet.length; i++){
            etaisyydet[i] = 800;
        }

        RoboOhjain instance = new RoboOhjain(bt,0,0,800);
        Point2D.Float expResult = new Point2D.Float(0.0F,650.0F); //laskimella väännetty
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of kokeileHakeaUusiMittauspiste method(sekava), of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste6() {
        System.out.println("kokeileHakeaUusiMittauspiste6");
        Point2D.Float nykySijainti = new Point2D.Float(0,0);
        float kulma = 0.0F;
        BTYhteys bt = new JsimBTYhteys();
        int[] etaisyydet = new int[37];
        for (int i = 0; i < etaisyydet.length; i++){
            etaisyydet[i] = 800;
        }

        etaisyydet[17] = 500;
        etaisyydet[18] = 500;
        etaisyydet[21] = 500;
        etaisyydet[25] = 500;
        etaisyydet[32] = 500;
        
        RoboOhjain instance = new RoboOhjain(bt,0,0,800);
        Point2D.Float expResult = new Point2D.Float(-184.3193342F,168.8975519F); //laskimella väännetty
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of kokeileHakeaUusiMittauspiste method(normiliike3/suunta 180), of class RoboOhjain.
     */
    @Test
    public void testKokeileHakeaUusiMittauspiste7() {
        System.out.println("kokeileHakeaUusiMittauspiste7");
        Point2D.Float nykySijainti = new Point2D.Float(0,0);
        float kulma = 180.0F;
        BTYhteys bt = new JsimBTYhteys();
        int[] etaisyydet = new int[37];
        for (int i = 0; i < etaisyydet.length; i++){
            etaisyydet[i] = 800;
        }
        etaisyydet[0] = 500;
        etaisyydet[36] = 500;
        etaisyydet[18] = 500;
        etaisyydet[17] = 500;
        
        RoboOhjain instance = new RoboOhjain(bt,0,0,800);
        Point2D.Float expResult = new Point2D.Float(-368.6386684F,-337.7951038F); //laskimella väännetty
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
    }

    /**
     * Test of kokeileLisataHavainnotKarttaan method, of class RoboOhjain.
     */
    @Test
    public void testKokeileLisataHavainnotKarttaan() {
        System.out.println("kokeileLisataHavainnotKarttaan");
        JsimBTYhteys bt = new JsimBTYhteys();
        RoboOhjain instance = new RoboOhjain(bt, 0, 0, 800);

        BTPaketti paketti = new BTPaketti(0);
        paketti.setNykySijainti(new Point2D.Float(500, 1500));
        paketti.setMittausSuunta(new Point2D.Float(500+100, 1500-100)); // Katse kaakkoon.
        int[] etaisyydet = {707, 184, 170, 184, 184, 141, 130, 141, 707};
        paketti.setEtaisyydet(etaisyydet);
        instance.asetaTestausPaketti(paketti);
        instance.kokeileLisataHavainnotKarttaan(paketti);

        paketti = new BTPaketti(1);
        paketti.setNykySijainti(new Point2D.Float(950, 650));
        paketti.setMittausSuunta(new Point2D.Float(950-100, 650+100)); // Katse luoteeseen.
        int[] etaisyydet2 = {113, 80, 113, 800, 566};
        paketti.setEtaisyydet(etaisyydet2);
        instance.asetaTestausPaketti(paketti);
        instance.kokeileLisataHavainnotKarttaan(paketti);
        final Line2D.Float[] result = instance.haeKartta();
        final Line2D.Float[] expResult = {
            new Line2D.Float(1000.62134f, 2000.6646f, 1000.62134f, 2000.6646f),
            new Line2D.Float(630.8121f, 1369.1766f, 630.8121f, 1369.1766f),
            new Line2D.Float(-0.6213379f, 999.33545f, -0.6213379f, 999.33545f),
            new Line2D.Float(445.65796f, 1368.8164f, 554.34204f, 1368.8164f),
            new Line2D.Float(670.9195f, 1429.2023f, 670.9195f, 1570.7977f),
            new Line2D.Float(1350.9214f, 1050.9214f, 1350.9214f, 1050.9214f),
            new Line2D.Float(869.3915f, 730.6085f, 869.3915f, 569.3915f),
        };

        for (int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i].x1, result[i].x1, 0.01);            
            assertEquals(expResult[i].y1, result[i].y1, 0.01);            
            assertEquals(expResult[i].x2, result[i].x2, 0.01);            
            assertEquals(expResult[i].y2, result[i].y2, 0.01);            
        }
    }
}
