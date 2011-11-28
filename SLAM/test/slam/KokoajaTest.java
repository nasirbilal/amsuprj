/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

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
        ArrayList<Float> vaaka0 = null;
        ArrayList<Float> vaaka1 = null;
        ArrayList<Float> pysty0 = null;
        ArrayList<Float> pysty1 = null;
        boolean expResult = false;
        boolean result = Kokoaja.kokeileKarttojenYhteensopivuutta(vaaka0, vaaka1, pysty0, pysty1);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
