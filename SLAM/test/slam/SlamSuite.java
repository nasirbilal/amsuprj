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
@Suite.SuiteClasses({UItempMainTest.class, KomentajaTest.class, JsimDataTest.class, JsimKarttaTest.class, JsimTestiTest.class, JsimRoboNäkymäTest.class, MittauksetTest.class, MainTest.class, UITest.class, JsimRoboTest.class, KarttaNakymaTest.class})
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
