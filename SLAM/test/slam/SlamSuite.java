/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Olli
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({JSimRoboNakymaTest.class, JsimRoboTest.class,
                     JsimBTYhteys.class, RoboOhjainTest.class,
                     KokoajaTest.class})
public class SlamSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
