/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @brief Kokoaja yhdistää robottien yksityiset kartat yhdeksi kokonaiskartaksi.
 * 
 * Kokoaja vastaanottaa yksittäisten robottien tutkimia alueita kuvaavat kartat
 * ja yhdistää ne sopivasta molemmista kartoista löytyvästä vastaavuuskohdasta
 * toisiinsa. Jos kartat ovat täysin erilliset tai algoritmi muutoin epäonnistuu
 * yhteisen alueen/kohdan löytämisessä, erilliset kartat asetellaan
 * sattumanvaraisesti päällekkäin.
 * 
 * Robottien tutkiman ympäristön oletetaan koostuvan suorista seinistä, jotka
 * ovat aina suorassa kulmassa toisiinsa nähden. Seinillä on oletuspaksuus,
 * jota lähempänä kaksi seinää ei koskaan ole ellei niillä ole ainakin yhtä
 * yhteistä kulmaa (jonka suuruus siis on aina 90 asteen moninkerta).
 * 
 * @author L
 */
public class Kokoaja {
    private static List<Line2D.Float> kartta;
    private static Map<Integer, Line2D.Float[] > roboKartta;
    private static boolean onMuuttunut;

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

        // kartat[0] lisättiin äsken. Aloita indeksistä 1.
        for (int k = 1; k < kartat.size(); ++k)
            for (int kulma = 0; kulma < 360; kulma += 90) {
            
            // Siirrä pisteitä kunnes ne ovat origossa.
            // Liitä pisteet karttaan.
        }

        onMuuttunut = false;
        return kartta.toArray(new Line2D.Float[kartta.size()]);
    }
}
