/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Point2D;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mudi
 */
public class NXTBTYhteysTest {

    /**
     * Test of lahetaJaVastaanota method, of class NXTBTYhteys.
     */
    @Test
    public void testLahetaJaVastaanota() {
        System.out.println("lahetaJaVastaanota");
        BTPaketti paketti = new BTPaketti(-1);
        paketti.setMittausSuunta(new Point2D.Float(9.0f, 10.0f));
        paketti.setNykySijaiti(new Point2D.Float(11.0f, 12.0f));
        paketti.setUusiSijaiti(new Point2D.Float(13.0f, 14.0f));

        int etaisyydet[] = new int[BTPaketti.MAARA];
        for (int i = 0; i < BTPaketti.MAARA; i++) {
            etaisyydet[i] = 80;
        }

        paketti.setEtaisyydet(etaisyydet);
        int odotusAikaMs = 5;
        NXTBTYhteys instance = new NXTBTYhteys(new JsimRobo("Jantunen"));
        instance.start();
        BTPaketti result = instance.lahetaJaVastaanota(paketti, odotusAikaMs);
        System.out.println(result.getId());
        assertEquals(1, result.getId());
    }
}
