/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
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
public class KokoajaTest {
    
    public KokoajaTest() {
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
     * Test of kokeileEtsiaMahdotontaRisteysta method, of class Kokoaja.
     */
    @Test
    public void testKokeileEtsiaMahdotontaRisteysta() {
        System.out.println("kokeileEtsiaMahdotontaRisteysta");
        ArrayList<Float> vaaka = null;
        ArrayList<Float> pysty = null;
        Double expResult = null;
        Double result = Kokoaja.kokeileEtsiaMahdotontaRisteysta(vaaka, pysty);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of kokeileKarttojenYhteensopivuutta method, of class Kokoaja.
     */
    @Test
    public void testKokeileKarttojenYhteensopivuutta() {
        System.out.println("kokeileKarttojenYhteensopivuutta");
        ArrayList<Line2D.Float> vaaka0 = new ArrayList<Line2D.Float>();
        ArrayList<Line2D.Float> vaaka1 = new ArrayList<Line2D.Float>();
        ArrayList<Line2D.Float> pysty0 = new ArrayList<Line2D.Float>();
        ArrayList<Line2D.Float> pysty1 = new ArrayList<Line2D.Float>();
        
        //Lisätään kartan viivat; alkuperäinen kartta
        vaaka0.add(new Line2D.Float(3,13,3,5));
        pysty0.add(new Line2D.Float(3,5,11,5));
        
        //alkuperäiseen karttaan lisättävä kartta
        vaaka1.add(new Line2D.Float(4,3,4,8));
        pysty1.add(new Line2D.Float(4,8,12,8));
        
        boolean expResult = false;
        boolean result = Kokoaja.kokeileKarttojenYhteensopivuutta(vaaka0, vaaka1, pysty0, pysty1);
        assertEquals(expResult, result);
    }

    /**
     * Test of kokeileSiistiaKarttaa method, of class Kokoaja.
     */
    @Test
    public void testKokeileSiistiaKarttaa() {
        System.out.println("kokeileSiistiaKarttaa");
        Float[] kartta = null;
        Float[] expResult = null;
        Float[] result = Kokoaja.kokeileSiistiaKarttaa(kartta);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
