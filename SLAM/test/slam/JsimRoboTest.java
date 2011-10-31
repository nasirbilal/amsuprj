/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
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
public class JsimRoboTest {
    
    public JsimRoboTest() {
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
     * Test of etene method, of class JsimRobo.
     */
    @Test
    public void testEtene() {
        System.out.println("etene");
        float matka = 500.0F;
        JsimRobo instance = new JsimRobo(90,0,0);
        Point2D.Float expResult =  new Point2D.Float(500.0f,0.0f);
        Point2D.Float result = instance.etene(matka);
        assertEquals(expResult, result);
    }

    /**
     * Test of etenePisteeseen method, of class JsimRobo.
     */
    @Test
    public void testEtenePisteeseen() {
        System.out.println("etenePisteeseen");
        Float kohde = null;
        JsimRobo instance = new JsimRobo();
        Float expResult = null;
        Float result = instance.etenePisteeseen(kohde);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of teleport method, of class JsimRobo.
     */
    @Test
    public void testTeleport() {
        System.out.println("teleport");
        Float kohde = null;
        JsimRobo instance = new JsimRobo();
        Float expResult = null;
        Float result = instance.teleport(kohde);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of käänny method, of class JsimRobo.
     */
    @Test
    public void testKäänny() {
        System.out.println("k\u00e4\u00e4nny");
        float aste = 0.0F;
        JsimRobo instance = new JsimRobo();
        float expResult = 0.0F;
        float result = instance.käänny(aste);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of käännyKohti method, of class JsimRobo.
     */
    @Test
    public void testKäännyKohti() {
        System.out.println("k\u00e4\u00e4nnyKohti");
        Float kohde = null;
        float bonusaste = 0.0F;
        JsimRobo instance = new JsimRobo();
        float expResult = 0.0F;
        float result = instance.käännyKohti(kohde, bonusaste);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of osoitaSuuntaan method, of class JsimRobo.
     */
    @Test
    public void testOsoitaSuuntaan() {
        System.out.println("osoitaSuuntaan");
        float suunta = 0.0F;
        JsimRobo instance = new JsimRobo();
        float expResult = 0.0F;
        float result = instance.osoitaSuuntaan(suunta);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valitseUusiPiste method, of class JsimRobo.
     */
    @Test
    public void testValitseUusiPiste() {
        System.out.println("valitseUusiPiste");
        JsimKartta JSKkartta = null;
        JsimRobo instance = new JsimRobo();
        JsimData expResult = null;
        JsimData result = instance.valitseUusiPiste(JSKkartta);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mittaa method, of class JsimRobo.
     */
    @Test
    public void testMittaa() {
        System.out.println("mittaa");
        JsimKartta JSKkartta = null;
        JsimRobo instance = new JsimRobo();
        JsimData expResult = null;
        JsimData result = instance.mittaa(JSKkartta);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
