/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D.Float;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author k8
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
        RoboOhjain instance = null;
        Double[] expResult = null;
        Double[] result = instance.haeEtaisyydet();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        Point2D.Float result = instance.kokeileHakeaUusiMittauspiste(nykySijainti, kulma, etaisyydet);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        instance.kokeileLisataHavainnotKarttaan(nykySijainti, kulma, etaisyydet);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
