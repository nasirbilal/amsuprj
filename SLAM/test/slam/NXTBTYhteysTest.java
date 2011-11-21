/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mudi
 */
public class NXTBTYhteysTest {
    
    public NXTBTYhteysTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of run method, of class NXTBTYhteys.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        NXTBTYhteys instance = new NXTBTYhteys(new JsimRobo("Jantunen"));
        instance.start();
        assert true;
    }

    /**
     * Test of lahetaJaVastaanota method, of class NXTBTYhteys.
     */
    @Test
    public void testLahetaJaVastaanota() {
        System.out.println("lahetaJaVastaanota");
        BTPaketti paketti = new BTPaketti(1);
        int odotusAikaMs = 5;
        NXTBTYhteys instance = new NXTBTYhteys(new JsimRobo("Jantunen"));
        instance.start();
        BTPaketti result = instance.lahetaJaVastaanota(paketti, odotusAikaMs);
        assertEquals(1, result.getId());
    }

    /**
     * Test of uudelleenKaynnista method, of class NXTBTYhteys.
     */
    @Test
    public void testUudelleenKaynnista() {
        System.out.println("uudelleenKaynnista");
        NXTBTYhteys instance = null;
        instance.uudelleenKaynnista();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
