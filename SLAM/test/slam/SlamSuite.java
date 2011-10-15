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
@Suite.SuiteClasses({slam.UItempMainTest.class, slam.RoboNakymaTest.class, slam.KomentajaTest.class, slam.MainTest.class, slam.UITest.class, slam.KarttaNakymaTest.class})
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
