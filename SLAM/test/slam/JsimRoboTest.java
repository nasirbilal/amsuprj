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
        JsimRobo instance = new JsimRobo(180, 0, 0, "jantunen");
        Point2D.Float expResult = new Point2D.Float(6.123234E-14f, -500);
        Point2D.Float result = instance.etene(matka);
        assertEquals(expResult, result);
    }

    /**
     * Test of etenePisteeseen method, of class JsimRobo.
     */
    @Test
    public void testEtenePisteeseen1() {
        System.out.println("etenePisteeseen");
        Point2D.Float kohde = new Point2D.Float(500.0f, 0.0f);
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        Point2D.Float expResult = new Point2D.Float(500.0f, 0.0f);
        Float result = instance.etenePisteeseen(kohde);
        assertEquals(expResult, result);
    }

    @Test
    public void testEtenePisteeseen2() {
        System.out.println("etenePisteeseen");
        Point2D.Float kohde = new Point2D.Float(10f, -10);
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        Point2D.Float expResult = new Point2D.Float(10f, -10);
        Float result = instance.etenePisteeseen(kohde);
        assertEquals(expResult, result);
    }

    @Test
    public void testEtenePisteeseen3() {
        System.out.println("etenePisteeseen");
        Point2D.Float kohde = new Point2D.Float(10f, 10);
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        Point2D.Float expResult = new Point2D.Float(10f, 10);
        Float result = instance.etenePisteeseen(kohde);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testEtenePisteeseen4() {
        System.out.println("etenePisteeseen");
        Point2D.Float kohde = new Point2D.Float(-10, -10);
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        Point2D.Float expResult = new Point2D.Float(-10, -10);
        Float result = instance.etenePisteeseen(kohde);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testEtenePisteeseen5() {
        System.out.println("etenePisteeseen");
        Point2D.Float kohde = new Point2D.Float(-10, 10);
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        Point2D.Float expResult = new Point2D.Float(-10, 10);
        Float result = instance.etenePisteeseen(kohde);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testEtenePisteeseen6() {
        System.out.println("etenePisteeseen");
        Point2D.Float kohde = new Point2D.Float(-63.764F, 47.007F);
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        Point2D.Float expResult = new Point2D.Float(-63.764F, 47.007F);
        Float result = instance.etenePisteeseen(kohde);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of käänny method, of class JsimRobo.
     */
    @Test
    public void testKäänny() {
        System.out.println("k\u00e4\u00e4nny");
        float aste = -40.0F;
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        float expResult = 320.0F;
        float result = instance.käänny(aste);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of kaannyKohti method, of class JsimRobo.
     */
    @Test
    public void testkaannyKohti1() {
        System.out.println("k\u00e4\u00e4nnyKohti");
        Point2D.Float kohde = new Point2D.Float(500.0f, 0.0f);
        float bonusaste = 0.0F;
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        float expResult = 90.0F;
        float result = instance.käännyKohti(kohde);
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testkaannyKohti2() {
        System.out.println("k\u00e4\u00e4nnyKohti");
        Point2D.Float kohde = new Point2D.Float(500.0f, 500.0f);
        float bonusaste = 0.0F;
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        float expResult = 45.0F;
        float result = instance.käännyKohti(kohde);
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testkaannyKohti3() {
        System.out.println("k\u00e4\u00e4nnyKohti");
        Point2D.Float kohde = new Point2D.Float(-10, 10);
        float bonusaste = 0.0F;
        JsimRobo instance = new JsimRobo(0, 0, 0, "jantunen");
        float expResult = 315.0F;
        float result = instance.käännyKohti(kohde);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of osoitaSuuntaan method, of class JsimRobo.
     */
    @Test
    public void testOsoitaSuuntaan() {
        System.out.println("osoitaSuuntaan");
        float suunta = 45.0F;
        JsimRobo instance = new JsimRobo("jantunen");
        float expResult = 45.0F;
        instance.setSuunta(suunta);
        float result = instance.getSuunta();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of valitseUusiPiste method, of class JsimRobo.
     */
    @Test
    public void testValitseUusiPiste() {
        System.out.println("valitseUusiPiste");
        JsimKartta JSKkartta = null;
        JsimRobo instance = new JsimRobo("jantunen");
        JsimData expResult = null;
        JsimData result = instance.valitseUusiPiste(37);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("ValitseUusiPiste ei ole valmis");
    }

    /**
     * Test of mittaa method, of class JsimRobo.
     */
    @Test
    public void testMittaa() {
        System.out.println("mittaa");
        JsimKartta JSKkartta = null;
        JsimRobo instance = new JsimRobo("Jaska");
        JsimData expResult = null;
        JsimData result = instance.mittaa(37);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
