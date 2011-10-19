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
public class SoftNXTTest {
    
    public SoftNXTTest() {
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
     * Test of rotate method, of class SoftNXT.
     */
    @Test
    public void testRotate() {
        System.out.println("rotate");
        double angles = 0.0;
        SoftNXT instance = new SoftNXT();
        instance.rotate(angles);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of travel method, of class SoftNXT.
     */
    @Test
    public void testTravel() {
        System.out.println("travel");
        double dist = 0.0;
        SoftNXT instance = new SoftNXT();
        instance.travel(dist);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of detDistancesOrSomething method, of class SoftNXT.
     */
    @Test
    public void testDetDistancesOrSomething() {
        System.out.println("detDistancesOrSomething");
        SoftNXT instance = new SoftNXT();
        double[] expResult = null;
        double[] result = instance.detDistancesOrSomething();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
