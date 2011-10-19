/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

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
public class MittauksetTest {
    
    public MittauksetTest() {
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
     * Test of getTaulu method, of class Mittaukset.
     */
    @Test
    public void testGetTaulu() {
        System.out.println("getTaulu");
        Mittaukset instance = null;
        float[] expResult = null;
        float[] result = instance.getTaulu();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
