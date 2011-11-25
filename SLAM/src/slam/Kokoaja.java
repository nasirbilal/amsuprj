/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @brief Kokoaja yhdistää robottien yksityiset kartat yhdeksi kokonaiskartaksi.
 * 
 * @author L
 */
public class Kokoaja {

    private static List<Line2D.Float> kartta; /// Lopullinen yhdistetty kartta.
    private static Map<Integer, Line2D.Float[]> roboKartta; /// Robojen kartat.
    private static boolean onMuuttunut; /// Jotain robokarttaa on muutettu.
    private static double beta; /// Kulma, jonka alapuolella "vierekkäiset"
    /// janat ovat "samansuuntaisia."
    private static double delta; /// Etäisyys, jonka sisällä kaksi janaa ovat
    /// "lähellä toisiaan."
    private static double seinanPaksuus; /// Seinän minimipaksuus.
    private static double robotinHalk; /// Robotin maksimihalkaisija.

    /** @brief Asettaa ulottuvuudet seinille ja robotille.
     * 
     * Ulottuvuuksia käytetään kartan rakenteiden fyysisen mahdollisuuden tai
     * mahdottomuuden varmentamiseen. Seinän paksuus kertoo sen, kuinka kaukana
     * kahden sellaisen seinän pitää toisistaan olla, jotka jakavat yhteisen
     * umpinaisen seinäalueen. Robotin halkaisijaa käytetään määrittelemään
     * kapeimman mahdollisen käytävän leveys. Jos kartassa on borotin suurinta
     * halkaisijaa kapeampia käytäviä, on kartta viallinen eli käyttökelvoton.
     * 
     * @param seinanPaksuus Minimipaksuus, joka kahden seinän välillä on.
     * @param robotinHalkaisija Robotin suurin halkaisija.
     */
    public static void asetaUlottuvuudet(double seinanPaksuus,
            double robotinHalkaisija) {
        Kokoaja.seinanPaksuus = seinanPaksuus;
        Kokoaja.robotinHalk = robotinHalkaisija;
    }

    /** @brief Asettaa virheensietorajat.
     * 
     * Virheensietorajoja käytetään, kun lyhyitä seinäpätkiä yhdistellään
     * suuremmiksi seinäkokonaisuuksiksi.
     * 
     * @param beta Kahden "samansuuntaisen" seinän välisen kulman maksimi.
     * @param delta  Kahden "vierekkäisen" seinän välisen etäisyyden maksimi.
     */
    public static void asetaVirhemitat(double beta, double delta) {
        Kokoaja.beta = beta;
        Kokoaja.delta = delta;
    }

    /** @brief Tallettaa robotin tutkiman kartan kokoajan muistiin.
     *
     * Robotin tutkiman ympäristön oletetaan koostuvan suorista seinistä, jotka
     * ovat aina suorassa kulmassa toisiinsa nähden. Seinillä on oletuspaksuus,
     * jota lähempänä kaksi seinää ei koskaan ole ellei niillä ole ainakin yhtä
     * yhteistä kulmaa (jonka suuruus siis on aina 0 tai 90 astetta).
     *
     * Seinien oletetaan olevan tallennetut muistiin siten, että jos seinää
     * pitkin katsotaan sitä kuvaavan janan alkupisteeseestä (p0), niin janan
     * oikeallepuolelle jää seinän sisäpuoli (umpinainen alue) ja vasemmalle
     * puolelle seinän ulkopuoli (lattia, ilmaa). Kun seinää pitkin katsotaan,
     * oletetaan, että seinä on karteesisessa koordinaatistossa, jossa X-akseli
     * kasvaa vasemmalta oikealle mentäessä ja Y-akseli kasvaa alhaalta ylöspäin
     * mentäessä.
     *
     * @param id Robotin identifioiva tunnusluku.
     * @param kartta Kartta robotin tutkimista alueista.
     * 
     * @warning Välitettyä karttaa muutetaan! Kopioi se ennen kutsua.
     */
    public static boolean asetaKartta(int id, Line2D.Float[] kartta) {
        if (roboKartta == null) {
            roboKartta = new TreeMap<Integer, Line2D.Float[]>();
        }

        kartta = siistiKartta(kartta);
        if (kartta != null) {
            roboKartta.put(id, kartta);
            onMuuttunut = true;
        }

        return kartta != null;
    }

    /** @brief Yhdistää aiemmin annetut robottien näkymät yhdeksi kartaksi.
     *
     * Metodi yhdistää yksittäisten robottien tutkimia alueita kuvaavat kartat
     * sopivasta molemmista kartoista löytyvästä vastaavuuskohdasta
     * toisiinsa. Jos kartat ovat täysin erilliset tai algoritmi muutoin
     * epäonnistuu yhteisen alueen/kohdan löytämisessä, erilliset kartat
     * asetellaan sattumanvaraisesti päällekkäin.
     *
     * @return yhdistetty , robottien yhteinen kartta.
     */
    public static Line2D.Float[] yhdista() {
        if (kartta == null) {
            kartta = new ArrayList<Line2D.Float>();
        }

        if (!onMuuttunut || roboKartta.isEmpty()) {
            return kartta.toArray(new Line2D.Float[kartta.size()]);
        }

        ArrayList<Line2D.Float> pysty0 = new ArrayList<Line2D.Float>();
        ArrayList<Line2D.Float> vaaka0 = new ArrayList<Line2D.Float>();
        ArrayList<Line2D.Float> pysty1 = new ArrayList<Line2D.Float>();
        ArrayList<Line2D.Float> vaaka1 = new ArrayList<Line2D.Float>();

        kartta.clear();
        ArrayList<Line2D.Float[]> kartat =new ArrayList<Line2D.Float[]>(roboKartta.values());
        kartta.addAll(Arrays.asList(kartat.get(0)));

        // Erittele alkulartta vaakasuoriin ja pystysuoriin seiniin.
        for (Line2D.Float l : kartat.get(0)) {
            float dx = l.x2 - l.x1;
            float dy = l.y2 - l.y1;

            if (dx * dx > dy * dy) {
                vaaka0.add(l);
            } else {
                pysty0.add(l);
            }
        }

        // kartat[0] on alkukartta, johon lisätään muut karttapalat yksi
        // kerrallaan. Aloitetaan heti seuraavasta kartasta, eli [1]:stä.
        for (int k = 1; k < kartat.size(); ++k) {
            boolean OK = false;

            for (int a = 0; !OK && a < 4; a++) {
                pysty1.clear();
                vaaka1.clear();

                // Käännä kartan seiniä 90 astetta ja lisää ne erillisiin
                // pystysuorien ja vaakasuorien seinien listoihin.
                for (Line2D.Float l : kartat.get(k)) {
                    float x = l.x1 * 0 - l.y1 * 1;
                    float y = l.y1 * 0 + l.x1 * 1;
                    l.x1 = x;
                    l.y1 = y;

                    x = l.x2 * 0 - l.y2 * 1;
                    y = l.y2 * 0 + l.x2 * 1;
                    l.x2 = x;
                    l.y2 = y;

                    float dx = l.x2 - l.x1;
                    float dy = l.y2 - l.y1;
                    if (dx * dx > dy * dy) {
                        vaaka1.add(l);
                    } else {
                        pysty1.add(l);
                    }
                }

                // Aseta vuorotellen jokainen jälkimmäisen kartan vaakasuorista
                // seinistä päällekkäin alkukartan jokaisen vaakasuoran seinän
                // kanssa. Jokaisen yhdistämisen kohdalla tee jokaiselle
                // pystysuoralle seinälle samoin.
                for (int i = 0; !OK && i < vaaka0.size(); ++i) {
                    for (int j = 0; !OK && j < vaaka1.size(); ++j) {
                        Line2D.Float lv0 = vaaka0.get(i);
                        Line2D.Float lv1 = vaaka1.get(j);
                        final float DY = lv1.y1 - lv0.y1;

                        float dx0 = lv0.x2 - lv0.x1;
                        float dx1 = lv1.x2 - lv1.x1;

                        // Jos janat kulkevat päinvastaiseen suuntaan, niin
                        // ei eivät voi olla yhteistä seinää.
                        if ((dx0 < 0 && dx1 > 0) || (dx0 > 0 && dx1 < 0)) {
                            continue;
                        }

                        for (int m = 0; !OK && m < pysty0.size(); ++m) {
                            for (int n = 0; !OK && n < pysty1.size(); ++n) {
                                Line2D.Float lp0 = pysty0.get(m);
                                Line2D.Float lp1 = pysty1.get(n);
                                final float DX = lp1.y1 - lp0.y1;

                                float dy0 = lv0.x2 - lv0.x1;
                                float dy1 = lv1.x2 - lv1.x1;

                                // Jos janat kulkevat päinvastaiseen suuntaan,
                                // niin ei eivät voi olla yhteistä seinää.
                                if ((dy0 < 0 && dy1 > 0) || (dy0 > 0 && dy1 < 0)) {
                                    continue;
                                }

                                // Sijoita kartta alkuperäisen päälle niin,
                                // että edellä valitut seinät asettuvat
                                // päällekkäin täsmälleen.
                                for (Line2D.Float l : kartat.get(k)) {
                                    l.x1 -= DX;
                                    l.x2 -= DX;
                                    l.y1 -= DY;
                                    l.y2 -= DY;
                                }

                                // Käy läpi KAIKKI molempien karttojen seinät.
                                // Jos mitään mahdottomuuksia ei löydy, niin
                                // yhdistä jälkimmäinen kartta ensimmäiseen
                                // siirtämällä jokaista janaa DX ja DY verran.
                                if (kartatSopivatYhteen(vaaka0, vaaka1, pysty0,
                                        pysty1)) {
                                    kartta.addAll(Arrays.asList(kartat.get(k)));
                                    OK = true;
                                }
                            }
                        }
                    }
                }
            }

            if (!OK) {
                // Karttoja ei saatu yhdistettyä. Liitä kartat kylmästi
                // päällekkäin juuri niinkuin ne sattuvat osumaan.
                kartta.addAll(Arrays.asList(kartat.get(k)));
            }
        }

        onMuuttunut = false;
        return kartta.toArray(new Line2D.Float[kartta.size()]);
    }

    /** @brief Yksinkertaistaa annetun kartan.
     *
     * Metodi yhdistää perättäiset seinänpätkät, poistaa päällekkäiset pätkät
     * ja kiertää kartan niin, että siinä on jäljellä vain pysty- ja vaaka-
     * suoria janoja.
     * 
     * @param kartta Kartta robotin tutkimista alueista.
     * @return Siistitty kartta tai null jos kartta on mahdotonta siitiä.
     * 
     * @todo Ennen kartan lisäämistä varmista, että kartassa ei ole tyhmiä,
     *       fyysisesti mahdottomia seinäjanoja.
     */
    private static Line2D.Float[] siistiKartta(Line2D.Float[] kartta) {
        int kartassaJanoja = kartta.length;

        // yhdistele kartan viivat toisiinsa luoden mahd. pitkiä suoria seiniä.
        for (int i = 0; i < kartassaJanoja; ++i) {
            for (int j = i + 1; j < kartassaJanoja; ++j) {
                Line2D.Float l0 = kartta[i];
                Line2D.Float l1 = kartta[j];

                double a0 = Math.atan2(l0.y2 - l0.y1, l0.x2 - l0.x1);
                double a1 = Math.atan2(l1.y2 - l1.y1, l1.x2 - l1.x1);
                a0 += (a0 < 0 ? 2*Math.PI : 0);
                a1 += (a1 < 0 ? 2*Math.PI : 0);
                if (Math.abs(a1 - a0) > beta) // Ovatko janat samansuuntaisia?
                {
                    continue;
                }

                // Janat ovat samansuuntaisia. Tarkista onko yhden alkupiste
                // lähellä toista janaa niin, että ne voitaisiin yhdistää.

                Rectangle rajat = l0.getBounds();
                rajat.x -= delta;
                rajat.y -= delta;
                rajat.width += 2 * delta;
                rajat.height += 2 * delta;

                if (!rajat.contains(l1.getP1())) {
                    // Jana[j] ei ala jana[i]:n läheltä. Testaa toisinpäin.                    
                    l0 = kartta[j];
                    l1 = kartta[i];

                    rajat = l0.getBounds();
                    rajat.x -= delta;
                    rajat.y -= delta;
                    rajat.width += 2 * delta;
                    rajat.height += 2 * delta;
                    if (!rajat.contains(l1.getP1())) {
                        continue; // Janat eivät ole toisiaan lähellä.
                    }
                }

                // Jana[1] EHKÄ alkaa jana[0]:n läheltä. Varmista läheisyys.
                if (l0.ptSegDist(l1.getP1()) > delta) {
                    continue;
                }

                // Valitse loppupisteeksi l0.P1():stä kauimmaisin piste.
                if (!l0.contains(l1.getP2())) {
                    l0.x2 = l1.x2;
                    l0.y2 = l1.y2;
                }

                // Janat on sulautettu toisiinsa. Poista lyhyempi jana.
                kartta[i] = l0;
                kartta[j--] = kartta[--kartassaJanoja];
            }
        }

        if (kartassaJanoja <= 0) {
            return null;
        }

        int[] kulmat = new int[360];
        int eniten = 0;
        int toiseksiEniten = 0;

        // Laske missä kulmassa (suhteessa vaakasuoraan X-akseliin) olevia
        // seiniä on kaikkein ja toiseksi eniten.
        for (Line2D.Float l : kartta) {
            int alpha = (int) Math.toDegrees(Math.atan2(l.y2 - l.y1, l.x2 - l.x1));
            alpha += (alpha < 0 ? 360 : 0);
            int i = alpha % 360;

            if (kulmat[i]++ > kulmat[eniten]) {
                toiseksiEniten = eniten;
                eniten = i;
            }
        }

        double kierto = eniten;

        // Jos eniten olevat kulmat ovat "samansuuntaisia", niin kierron
        // määräksi kulmien keskiarvo.
        if (Math.abs(eniten - toiseksiEniten) < beta) {
            kierto = (double) (eniten + toiseksiEniten) * 0.5;
        }

        kierto = -Math.toRadians(kierto);

        // Käännä kartta niin, että kaikki sen seinät olisivat joko pysty-
        // tai vaakasuoria.
        for (int i = 0; i < kartassaJanoja; ++i) {
            double cos = Math.cos(kierto);
            double sin = Math.sin(kierto);

            double x = kartta[i].x1 * cos - kartta[i].y1 * sin;
            double y = kartta[i].y1 * cos + kartta[i].x1 * sin;
            kartta[i].x1 = (float) x;
            kartta[i].y1 = (float) y;

            x = kartta[i].x2 * cos - kartta[i].y2 * sin;
            y = kartta[i].y2 * cos + kartta[i].x2 * sin;
            kartta[i].x2 = (float) x;
            kartta[i].y2 = (float) y;

            float dx = kartta[i].x2 - kartta[i].x1;
            float dy = kartta[i].y2 - kartta[i].y1;

            // Pakota janat pysty- tai vaakasuoriksi. Vaihtoehtoisesti tässä 
            // voitaisiin tarkistaa, onko jana vino ja palauttaa virhe jos on.
            if (dx * dx > dy * dy) {
                kartta[i].y2 = kartta[i].y1;
            } else {
                kartta[i].x2 = kartta[i].x1;
            }
        }

        // Kopioi korjatut janat säiliöön lisättävään taulukkoon.
        // Näin vältetään turhien/virheellisten janojen tallentaminen.
        Line2D.Float[] siistiKartta = new Line2D.Float[kartassaJanoja];
        for (int i = 0; i < kartassaJanoja; ++i) {
            siistiKartta[i] = kartta[i];
        }

        return siistiKartta;
    }

    /** @brief Tarkistaa, ettei kartoissa ole fyysisesti mahdottomia kohtia.
     * 
     * @param vaaka0 Alkukartan vaakasuorat seinät.
     * @param vaaka1 Lisättävän kartan vaakasuorat seinät.
     * @param pysty0 Alkukartan pystysuorat seinät.
     * @param pysty1 Lisättävän kartan pystysuorat seinät.
     * @return Epätosi jos kartoista löytyy mahdoton yhdyskohta, muutoin tosi.
     */
    private static boolean kartatSopivatYhteen(ArrayList<Line2D.Float> vaaka0,
            ArrayList<Line2D.Float> vaaka1,
            ArrayList<Line2D.Float> pysty0,
            ArrayList<Line2D.Float> pysty1) {

        // Kokeile, etteivät pysty- ja vaakasuorat seinät muodosta mahdottomia
        // risteyksiä eli sellaisia, jossa yksi jana väittää jossakin seinien
        // yhteisessä pisteessä olevan umpinasta seinää ja toinen taas väittää
        // siinä olevan avonaista lattiaa. Tässä pitää tietää seinän paksuus
        // ja robotin halkaisija.
        if (etsiMahdotonRisteys(vaaka0, pysty1) != null
                || etsiMahdotonRisteys(vaaka1, pysty0) != null) {
            return false;
        }

        final double suurinVali = 2 * Math.max(seinanPaksuus, robotinHalk);
        final double e = 0.00001; // Epsilon: pyöristystä varten.
        Rectangle2D umpi0, avoin0, umpi1, avoin1, leikkaus;

        // Kokeile, etteivät vaakasuorat seinät muodosta mahdottomia
        // yhdistelmiä samoin kuin kokeiltiin mahdottomien risteysten kohdalla.
        for (Line2D.Float l0 : vaaka0) {
            for (Line2D.Float l1 : vaaka1) {
                if (Math.abs(l0.y1 - l1.y1) > suurinVali) {
                    continue;
                }

                if (l0.x1 < l0.x2) {
                    umpi0 = new Rectangle.Double(l0.x1, l0.y1 - seinanPaksuus,
                            l0.x2 - l0.x1, seinanPaksuus - e);
                    avoin0 = new Rectangle.Double(l0.x1, l0.y1 + e,
                            l0.x2 - l0.x1, robotinHalk - e);
                } else {
                    umpi0 = new Rectangle.Double(l0.x2, l0.y1 + e,
                            l0.x1 - l0.x2, seinanPaksuus - e);
                    avoin0 = new Rectangle.Double(l0.x2, l0.y1 - robotinHalk,
                            l0.x1 - l0.x2, robotinHalk - e);
                }

                if (l1.x1 < l1.x2) {
                    umpi1 = new Rectangle.Double(l1.x1, l1.y1 - seinanPaksuus,
                            l1.x2 - l1.x1, seinanPaksuus - e);
                    avoin1 = new Rectangle.Double(l1.x1, l1.y1 + e,
                            l1.x2 - l1.x1, robotinHalk - e);
                } else {
                    umpi1 = new Rectangle.Double(l1.x2, l1.y1 + e,
                            l1.x1 - l1.x2, seinanPaksuus - e);
                    avoin1 = new Rectangle.Double(l1.x2, l1.y1 - robotinHalk,
                            l1.x1 - l1.x2, robotinHalk - e);
                }

                leikkaus = umpi0.createIntersection(avoin1);
                if (leikkaus != null && leikkaus.getWidth() > e
                        && leikkaus.getHeight() > e) {
                    return false;
                }

                leikkaus = umpi1.createIntersection(avoin0);
                if (leikkaus != null && leikkaus.getWidth() > e
                        && leikkaus.getHeight() > e) {
                    return false;
                }
            }
        }

        // Kokeile, etteivät pystysuorat seinät muodosta mahdottomuuksia.
        for (Line2D.Float l0 : pysty0) {
            for (Line2D.Float l1 : pysty1) {
                if (Math.abs(l0.x1 - l1.x1) > suurinVali) {
                    continue;
                }

                if (l0.y1 < l0.y2) {
                    umpi0 = new Rectangle.Double(l0.x1 + e, l0.y1,
                            seinanPaksuus - e, l0.y2 - l0.y1);
                    avoin0 = new Rectangle.Double(l0.x1 - robotinHalk, l0.y1,
                            robotinHalk - e, l0.y2 - l0.y1);
                } else {
                    umpi0 = new Rectangle.Double(l0.x1 + e, l0.y2,
                            robotinHalk - e, l0.y1 - l0.y2);
                    avoin0 = new Rectangle.Double(l0.x1 - seinanPaksuus, l0.y2,
                            seinanPaksuus - e, l0.y1 - l0.y2);
                }

                if (l1.y1 < l1.y2) {
                    umpi1 = new Rectangle.Double(l1.x1 + e, l1.y1,
                            seinanPaksuus - e, l1.y2 - l1.y1);
                    avoin1 = new Rectangle.Double(l1.x1 - robotinHalk, l1.y1,
                            robotinHalk - e, l1.y2 - l1.y1);
                } else {
                    umpi1 = new Rectangle.Double(l1.x1 + e, l1.y2,
                            robotinHalk - e, l1.y1 - l1.y2);
                    avoin1 = new Rectangle.Double(l1.x1 - seinanPaksuus, l1.y2,
                            seinanPaksuus - e, l1.y1 - l1.y2);
                }

                leikkaus = umpi0.createIntersection(avoin1);
                if (leikkaus != null && leikkaus.getWidth() > e
                        && leikkaus.getHeight() > e) {
                    return false;
                }

                leikkaus = umpi1.createIntersection(avoin0);
                if (leikkaus != null && leikkaus.getWidth() > e
                        && leikkaus.getHeight() > e) {
                    return false;
                }
            }
        }

        return true; // "This is a match made in heaven."
    }

    private static Point2D.Double etsiMahdotonRisteys(
            ArrayList<Line2D.Float> vaaka,
            ArrayList<Line2D.Float> pysty) {
        final double suurinVali = 2 * Math.max(seinanPaksuus, robotinHalk);
        final double e = 0.00001; // Epsilon: pyöristystä varten.

        for (Line2D.Float lv : vaaka) {
            for (Line2D.Float lp : pysty) {
                if (lv.y1 < lp.y1 - suurinVali || lv.y1 > lp.y2 + suurinVali
                        || lp.x1 < lv.x1 - suurinVali || lp.x1 > lv.x2 + suurinVali) {
                    continue; // Seinillä ei ole lainkaan yhteistä aluetta.
                }
                // Luo molemmille seinäpaloille omat umpinaista ja avointa
                // aluetta kuvaavat nelikulmiot. Jos yhden umpinainen alue
                // menee päällekkäin toisen avoimen alueen kanssa, niin olemme
                // löytäneet mahdottoman risteyksen.
                Rectangle2D vUmpi, vAvoin, pUmpi, pAvoin, leikkaus;

                // Janan suunta vaikuttaa seinän rakenteeseen. Seinä on
                // määritelty niin, että seisottaessa sen alkupisteessä ja
                // katsottaessa seinää pitkin jää umpinainen alue seinää
                // kuvaavan janan oikealle puolelle ja avoin alue vasemmalle.
                // Käytössä on karteesinen koordinaatisto, jossa Y-akseni
                // kasvaa ylöspäin mentäessä.
                //
                // Epsilon erottaa suorakulmiot seinäpätkästä niin, ettei
                // törmäiystarkistuksessa tarvitse erikseen käsitellä tapausta,
                // jossa alueiden jokin reuna menee täsmälleen päällekkäin.
                if (lv.x1 < lv.x2) {
                    vUmpi = new Rectangle.Double(lv.x1, lv.y1 - seinanPaksuus,
                            lv.x2 - lv.x1, seinanPaksuus - e);
                    vAvoin = new Rectangle.Double(lv.x1, lv.y1 + e,
                            lv.x2 - lv.x1, robotinHalk - e);
                } else {
                    vUmpi = new Rectangle.Double(lv.x2, lv.y1 + e,
                            lv.x1 - lv.x2, seinanPaksuus - e);
                    vAvoin = new Rectangle.Double(lv.x2, lv.y1 - robotinHalk,
                            lv.x1 - lv.x2, robotinHalk - e);
                }

                if (lp.y1 < lp.y2) {
                    pUmpi = new Rectangle.Double(lp.x1 + e, lp.y1,
                            seinanPaksuus - e, lp.y2 - lp.y1);
                    pAvoin = new Rectangle.Double(lp.x1 - robotinHalk, lp.y1,
                            robotinHalk - e, lp.y2 - lp.y1);
                } else {
                    pUmpi = new Rectangle.Double(lp.x1 + e, lp.y2,
                            robotinHalk - e, lp.y1 - lp.y2);
                    pAvoin = new Rectangle.Double(lp.x1 - seinanPaksuus, lp.y2,
                            seinanPaksuus - e, lp.y1 - lp.y2);
                }

                // Kokeile suorakulmioita päällekkäin. Jos jonkin avoimen
                // kulmion kulma on jonkin umpinaisen kulmion sisällä, niin
                // palauta virhe.
                leikkaus = vUmpi.createIntersection(pAvoin);
                if (leikkaus != null && leikkaus.getWidth() > e
                        && leikkaus.getHeight() > e) {
                    return new Point2D.Double(leikkaus.getCenterX(),
                            leikkaus.getCenterY());
                }

                leikkaus = pUmpi.createIntersection(vAvoin);
                if (leikkaus != null && leikkaus.getWidth() > e
                        && leikkaus.getHeight() > e) {
                    return new Point2D.Double(leikkaus.getCenterX(),
                            leikkaus.getCenterY());
                }
            }
        }

        return null;
    }
}
