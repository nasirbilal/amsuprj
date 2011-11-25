/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @brief Luokka ohjaa robottia ja käsittelee siltä tulevaa dataa.
 * 
 * Bluetooth-yhteyden avulla luokka vastaanottaa robotilta sen sijaintitiedot
 * ja kerätyt mittaustulokset. Luokka vastaa mittaustulosten yhdistämisestä
 * robotin henkilökohtaisesti kartoittamaksi kartaksi ja sen perusteella robotin
 * laskennallisen sijainnin määrittämisestä. Kaikki kommunikointi robotin kanssa
 * tapahtuu tämän luokan kautta. Jokaista robottia vastaa yksi RoboOhjain-
 * loukan ilmentymä.
 */
public class RoboOhjain extends Thread {

    private int odotusMs; /// Kauanko BT-yhteyttä odotetaan ennen time outia.
    private boolean onMuuttunut; /// Onko tullut BT:ltä uutta dataa.
    private int maxEtaisyys; /// Etäisyys, jota kauempia esteitä ei havaita.
    private BTYhteys bt; /// BT-yhteys oikeaan tai simuloituun robottiin.
    private BTPaketti paketti; /// Viimeisin saatu tulos Bluetoothilta.
    private Point2D.Double[] roboNakyma; /// Mittaustulokset koordinaatistossa.
    private Line2D.Float[] mittausJanat; /// Robotin mittaussuuntien janat.
    private ArrayList<Line2D.Float> kartta; /// Robotin tutkima alue.

    /** @brief Alustaa robotin ohjaimen.
     * 
     * @param bt Bluetooth-yhteys robottiin, jota tämä instanssi ohjaa.
     * @param ID Robotin (ja samalla tämän ohjaimen) tunnusluku.
     * @param odotusMilliSek Odotusika, jonka kuluttua BT-yhteys alustetaan.
     */
    RoboOhjain(BTYhteys bt, int ID, int odotusMilliSek, int maxEtaisyys) {
        this.odotusMs = odotusMilliSek;
        this.onMuuttunut = false;
        this.maxEtaisyys = maxEtaisyys;
        this.bt = bt;
        this.paketti = new BTPaketti(ID);
        this.paketti.setNykySijaiti(new Point2D.Float(0, 0));
        this.paketti.setUusiSijaiti(new Point2D.Float(0, 0));
        this.paketti.setMittausSuunta(new Point2D.Float(0, 0));
        this.roboNakyma = new Point2D.Double[BTPaketti.MAARA];
        this.kartta = new ArrayList<Line2D.Float>();

        JsimRoboNakyma nakyma = new JsimRoboNakyma(new Point2D.Float(0, 0),
                0, paketti.getEtaisyydet().length, 1);
        this.mittausJanat = nakyma.getNakotaulu();
    }

    /** @brief Palauttaa robotin mittaustulokset sijoitettuna koordinaatistoon.
     *
     * Robotin viimeksi mittaamat etäisyydet sijoitellaan mittauskulmien
     * perusteella karteesiseen koordinaatistoon niin, että robotin ollessa
     * origossa ja katsoesa positiivisen Y-akselin suuntaan havaitut esteet
     * sijaitsevat oikeassa suhteessa robottiin.
     *
     * @return Lista robotin suhteen sijoitelluista mittausetäisyyksistä. Jos
     *         estettä ei havaittu eli mittausetäisyys on ääretön, kyseinen
     *         koordinaatti on null.
     */
    public Point2D.Double[] haeEtaisyydet() {
        if (!onMuuttunut) {
            return roboNakyma;
        }

        final int[] etaisyydet = paketti.getEtaisyydet();
        for (int i = 0; i < mittausJanat.length; ++i) {
            if (etaisyydet[i] > maxEtaisyys) {
                roboNakyma[i] = null;
            } else {
                roboNakyma[i] = new Point2D.Double();
                roboNakyma[i].x = mittausJanat[i].x2 * etaisyydet[i];
                roboNakyma[i].y = mittausJanat[i].y2 * etaisyydet[i];
            }
        }

        onMuuttunut = false;
        return roboNakyma;
    }

    /** @brief Muodostaa robotin mittaustuloksista yhtenäisen kartan.
     *
     * Robotin kokoamista mittaustuloksista kootaan yksi yhtenäinen kartta.
     * Jos uusia mittaustuloksia ei ole saapunut, palautetaan viimeksi kasattu
     * kartta. Karttojen origo on sama kuin robotin 1. mittauspiste.
     *
     * @return Taulukko robotin havaitsemista seinistä/esteistä.
     */
    public Line2D.Float[] haeKartta() {
        return kartta.toArray(new Line2D.Float[kartta.size()]);
    }

    /** @return True jos uutta dataa on saapunut viime kyselyn jälkeen. */
    public boolean onMuuttunut() {
        return onMuuttunut;
    }

    /** @brief Robotti siirretään uuteen sijaintiin ja suoritetaan mittaukset.
     *
     * Robotille lasketaan uusi mittauspiste ja mittaussuunta. Tiedot
     * lähetetään BT:n kautta robotille. Metodi odottaa, että robotti saa
     * mittaukset tehtyä ja vastaanottaa mittaustulokset BT:n kautta.
     *
     * @return True, jos kaikki meni hyvin ja false, jos mittaustuloksia ei
     *  saatu odotusajan umpeuduttua.
     */
    private boolean teeMittaukset() {
        BTPaketti vastaus = bt.lahetaJaVastaanota(paketti, odotusMs);
        if (vastaus == null) {
            return false;
        }

        paketti = vastaus; // Talleta uusimmat tulokset.
        float dx = paketti.getMittausSuunta().x - paketti.getNykySijaiti().x;
        float dy = paketti.getMittausSuunta().y - paketti.getNykySijaiti().y;
        float alpha = (float) Math.atan2(dy, dx);
        alpha += (alpha < 0 ? 2*Math.PI : 0);
        int maara = paketti.getEtaisyydet().length;
        int[] etaisyydet = paketti.getEtaisyydet();
        JsimRoboNakyma nakyma = new JsimRoboNakyma(paketti.getNykySijaiti(),
                alpha, maara, 1);
        Line2D.Float[] sateet = nakyma.getNakotaulu();

        // Lisää robotin havaitsemat esteet karttaan.
        for (int i = 1; i < maara; ++i) {
            if (etaisyydet[i - 1] < maxEtaisyys && etaisyydet[i] < maxEtaisyys) {
                kartta.add(new Line2D.Float(sateet[i - 1].getP2(),
                        sateet[i].getP2()));
            }
        }

        // Laske robotin sijainti kartan datan perusteella.
        // TODO.

        // Laske robotille uusi mittauspiste ja -suunta.
        // Luo neljä sädettä robotista pääilmansuuntiin ja suunnista kohti sitä
        // loppupistettä, joka on etäämmällä robotista.
        float x = paketti.getNykySijaiti().x;
        float y = paketti.getNykySijaiti().y;
        Line2D.Float[] suunnat = {
            new Line2D.Float(paketti.getNykySijaiti(), new Point2D.Float(
            x, y - 800 * 100)),
            new Line2D.Float(paketti.getNykySijaiti(), new Point2D.Float(
            x - 800 * 100, y)),
            new Line2D.Float(paketti.getNykySijaiti(), new Point2D.Float(
            x, y + 800 * 100)),
            new Line2D.Float(paketti.getNykySijaiti(), new Point2D.Float(
            x + 800 * 100, y))
        };

        for (Line2D.Float i : suunnat) {
            for (Line2D.Float j : kartta) {
                if (j.intersectsLine(i)) {
                    j.x2 = (i.x1 + i.x2) / 2;
                    j.y2 = (i.y1 + i.y2) / 2;
                }
            }
        }

        Line2D.Float pisin = suunnat[0];
        dx = pisin.x2 - pisin.x1;
        dy = pisin.y2 - pisin.y1;
        float pisinPituus = dx * dx + dy * dy;
        for (int i = 1; i < suunnat.length; ++i) {
            dx = suunnat[i].x2 - suunnat[i].x1;
            dy = suunnat[i].y2 - suunnat[i].y1;
            if (dx * dx + dy * dy > pisinPituus) {
                pisin = suunnat[i];
            }
            pisinPituus = dx * dx + dy * dy;
        }

        dx = pisin.x2 - pisin.x1;
        dy = pisin.y2 - pisin.y1;
        dx = Math.min(dx, 10); // Kymmenen senttiä (?) eteenpäin.
        dy = Math.min(dy, 10); // Kymmenen senttiä (?) eteenpäin.
        paketti.setUusiSijaiti(new Point2D.Float(x + dx, y + dy));

        onMuuttunut = true;
        return true;
    }

    @Override
    public void run() {
        long i = 0;
        while (true) {
            System.out.println(Calendar.getInstance().getTime().toString() +
                               " kierros " + i++ + ".");
            if (!teeMittaukset()) {
                System.out.println("Mittaukset epäonnistuivat.");
            }
        }
    }
}
