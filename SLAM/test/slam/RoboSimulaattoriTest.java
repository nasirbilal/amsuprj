/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D.Float;
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
public class RoboSimulaattoriTest {
    
    public RoboSimulaattoriTest() {
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
     * Test of getX method, of class RoboSimulaattori.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        RoboSimulaattori instance = new RoboSimulaattori();
        double expResult = 0.0;
        double result = instance.getX();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getY method, of class RoboSimulaattori.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        RoboSimulaattori instance = new RoboSimulaattori();
        double expResult = 0.0;
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSuunta method, of class RoboSimulaattori.
     */
    @Test
    public void testGetSuunta() {
        System.out.println("getSuunta");
        RoboSimulaattori instance = new RoboSimulaattori();
        float expResult = 0.0F;
        float result = instance.getSuunta();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of etene method, of class RoboSimulaattori.
     */
    @Test
    public void testEtene() {
        System.out.println("etene");
        int matka = 0;
        RoboSimulaattori instance = new RoboSimulaattori();
        instance.etene(matka);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of käänny method, of class RoboSimulaattori.
     */
    @Test
    public void testKäänny() {
        System.out.println("k\u00e4\u00e4nny");
        int aste = 0;
        RoboSimulaattori instance = new RoboSimulaattori();
        instance.käänny(aste);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mittaa method, of class RoboSimulaattori.
     */
    @Test
    public void testMittaa() {
        System.out.println("mittaa");
        RoboSimulaattori instance = new RoboSimulaattori();
        Mittaukset expResult = null;
        Mittaukset result = instance.mittaa();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of risteys method, of class RoboSimulaattori.
     */
    @Test
    public void testRisteys() {
        System.out.println("risteys");
        Float roboNäkö = null;
        Float l = null;
        RoboSimulaattori instance = new RoboSimulaattori();
        float[] expResult = null;
        float[] result = instance.risteys(roboNäkö, l);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Xsuuntima method, of class RoboSimulaattori.
     */
    @Test
    public void testXsuuntima() {
        System.out.println("Xsuuntima");
        RoboSimulaattori instance = new RoboSimulaattori();
        float expResult = 0.0F;
        float result = instance.Xsuuntima();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Ysuuntima method, of class RoboSimulaattori.
     */
    @Test
    public void testYsuuntima() {
        System.out.println("Ysuuntima");
        RoboSimulaattori instance = new RoboSimulaattori();
        float expResult = 0.0F;
        float result = instance.Ysuuntima();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of perusLoop method, of class RoboSimulaattori.
     */
    @Test
    public void testPerusLoop() {
        System.out.println("perusLoop");
        RoboSimulaattori instance = new RoboSimulaattori();
        instance.perusLoop();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of luoRoboViivat method, of class RoboSimulaattori.
     */
    @Test
    public void testLuoRoboViivat() {
        System.out.println("luoRoboViivat");
        RoboSimulaattori instance = new RoboSimulaattori();
        Float[] expResult = null;
        Float[] result = instance.luoRoboViivat();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
