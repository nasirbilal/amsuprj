/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Kokoaja yhdistää robottien yksityiset kartat kokonaiseksi yhteiskartaksi.
 * 
 * @author L
 */
public class Kokoaja {
    private static List<Line2D.Float> kartta;
    private static Map<Integer, Line2D.Float[] > roboKartta;
    private static boolean onMuuttunut;

    private static final int TARKKUUSASTEET = 8;

    Kokoaja() {
        kartta = new ArrayList<Line2D.Float>();
        roboKartta = new TreeMap<Integer, Line2D.Float[] >();
        onMuuttunut = false;
    }

    /// @brief Tallettaa robotin tutkiman kartan kokoajan muistiin.
    void asetaKartta(int id, Line2D.Float[] kartta) {
        roboKartta.put(id, kartta);
        onMuuttunut = true;
    }

    /// @brief Yhdistää aiemmin annetut robottien näkymät yhdeksi kartaksi.
    Line2D.Float[] yhdista() {
        
        if (!onMuuttunut || roboKartta.isEmpty())
            return kartta.toArray(new Line2D.Float[kartta.size()]);
        
        kartta.clear();
        ArrayList<Line2D.Float[] > kartat = (ArrayList<Line2D.Float[] >) roboKartta.values();
        kartta.addAll(Arrays.asList(kartat.get(0)));

        for (int k = 1; k < kartat.size(); ++k) // kartat[0] lisättiin äsken.
          for (int tark = TARKKUUSASTEET; tark >= 0 ; --tark )
            for (int kulma = 0; kulma < 360; kulma += (1 + tark) * 10) {
            
            // Siirrä pisteitä kunnes ne ovat origossa.
            // Liitä pisteet karttaan.
        }

        onMuuttunut = false;
        return kartta.toArray(new Line2D.Float[kartta.size()]);
    }
}
