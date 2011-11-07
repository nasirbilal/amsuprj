/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Kokoaja yhdistää robottien mittaustulokset kokonaiseksi kartaksi.
 * 
 * @author L
 */
public class Kokoaja {
    private static List<Line2D.Float> map;

    Kokoaja() {
        map = new ArrayList<Line2D.Float>();
    }

    Line2D.Float[] liitaKarttaan(Point2D.Float sijainti, float suunta,
                                 float[] mittaustulokset) {
        // Pyöritä pisteitä kunnes ne ovat nollakulmassa.
        // Siirrä pisteitä kunnes ne ovat origossa.
        // Liitä pisteet karttaan.
        // ota huomioon, että esim. ekat mittaustulokset tulevat kahdelta tai
        // useammalta robotilta mukamas origosta ja nollakulmassa...
        // Mutta eihän niissä ole mitään samaa. Sekin on riski, että liitetään
        // kaksi väärää palaa toisiinsa, koska niissä on yhtä pitkä suora seinä tai jotain...
        // ehkä pitäisi kartat eritellä roboteittain ja sitten yhdistellä niitä?
        return null;
    }
}
