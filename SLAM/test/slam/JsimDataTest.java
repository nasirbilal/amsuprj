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
public class JsimDataTest {
    
    public JsimDataTest() {
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
     * Test of getRobosuunta method, of class JsimData.
     */
    @Test
    public void testGetRobosuunta() {
        System.out.println("getRobosuunta");
        JsimData instance = null;
        float expResult = 0.0F;
        float result = instance.getRobosuunta();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getData method, of class JsimData.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        JsimData instance = null;
        float[] expResult = null;
        float[] result = instance.getData();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
