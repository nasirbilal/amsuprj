package slam;

import java.awt.geom.*;
import java.util.*;

/**
 * @brief Luokka ohjaa robottia ja käsittelee siltä tulevaa dataa.
 * 
 * Bluetooth-yhteyden avulla luokka vastaanottaa robotilta sen sijaintitiedot
 * ja kerätyt mittaustulokset. Luokka vastaa mittaustulosten yhdistämisestä
 * robotin henkilökohtaisesti kartoittamaksi kartaksi ja sen perusteella robotin
 * laskennallisen sijainnin määrittämisestä. Kaikki kommunikointi robotin kanssa
 * tapahtuu tämän luokan kautta. Jokaista robottia vastaa yksi RoboOhjain-
 * luokan ilmentymä.
 */
public class RoboOhjain extends Thread {

    private int odotusMs; /// Kauanko BT-yhteyttä odotetaan ennen time outia.
    private boolean onMuuttunut; /// Onko tullut BT:ltä uutta dataa.
    private int maxEtaisyys; /// Etäisyys, jota kauempia esteitä ei havaita.
    private BTYhteys bt; /// BT-yhteys oikeaan tai simuloituun robottiin.
    private BTPaketti uusinPaketti; /// Viimeisin saatu tulos Bluetoothilta.
    private Point2D.Double[] roboNakyma; /// Mittaustulokset koordinaatistossa.
    private Line2D.Float[] mittausJanat; /// Robotin mittaussuuntien janat.
    private ArrayList<Line2D.Float> kartta; /// Robotin tutkima alue.
    private boolean kayttajaltaKoordinaatit;
    private Point2D.Float annettuPiste;   /// Käyttäjän antama piste. Jos käyttäjä antaa pisteen, sinne kuljetaan aina.
    private RoboSuunnistin suunnistin;

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
        this.uusinPaketti = bt.annaOletusPaketti();
        this.roboNakyma = new Point2D.Double[BTPaketti.MAARA];
        this.kartta = new ArrayList<Line2D.Float>();
        this.suunnistin = new RoboSuunnistin(maxEtaisyys);

        JsimRoboNakyma nakyma = new JsimRoboNakyma(new Point2D.Float(0, 0),
                0, uusinPaketti.getEtaisyydet().length, 1);
        this.mittausJanat = nakyma.getNakotaulu();
    }

    /**
     * 
     * @return
     */
    public Line2D.Float[] annaKoordinaatit(){
        int[] etaisyydet = uusinPaketti.getEtaisyydet();
        JsimRoboNakyma nakyma = new JsimRoboNakyma(uusinPaketti.getNykySijainti(),
                (float)Math.toDegrees(uusinPaketti.getMittausKulma()), etaisyydet.length, 1);
        Line2D.Float[] taulu = nakyma.getNakotaulu();
        
        for (int i = 0; i < etaisyydet.length; ++i) {
            taulu[i].x2 += (taulu[i].x2 - taulu[i].x1) * etaisyydet[i];
            taulu[i].y2 += (taulu[i].y2 - taulu[i].y1) * etaisyydet[i];
        }
        
        return taulu;
    }

    /**
     * Aseta Bluetooth-paketti ohjaimelle testausta varten.
     * @param paketti Valmiiksi alustettu paketti.
     */
    public void asetaTestausPaketti(BTPaketti paketti) {
        this.uusinPaketti = paketti;
        JsimRoboNakyma nakyma = new JsimRoboNakyma(new Point2D.Float(0, 0),
                0, paketti.getEtaisyydet().length, 1);
        this.mittausJanat = nakyma.getNakotaulu();
        this.roboNakyma = new Point2D.Double[paketti.getEtaisyydet().length];
        this.onMuuttunut = true;
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

        final int[] etaisyydet = uusinPaketti.getEtaisyydet();
        if (mittausJanat.length != etaisyydet.length)
            return null;
        
        for (int i = 0; i < mittausJanat.length; ++i) {
            if (etaisyydet[i] >= maxEtaisyys) {
                roboNakyma[i] = null;
            } else {
                roboNakyma[i] = new Point2D.Double();
                // Kyllä, tämä näyttää väärältä, mutta näin se nyt vaan menee!
                roboNakyma[i].y =  mittausJanat[i].x2 * etaisyydet[i];
                roboNakyma[i].x = -mittausJanat[i].y2 * etaisyydet[i];
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
        try {
            return kartta.toArray(new Line2D.Float[kartta.size()]);
        } catch (Exception ex) {
            System.out.println("\nKartan haku epäonnistui!");
            return new Line2D.Float[0];
        }
    }
    
    //Käyttäjä klikkaa kartalla pistettä ja käskemme robottia liikkumaan siihen pisteeseen
    //emmekä laske erikseen uutta pistettä.
    /**
     * 
     * @param p
     */
    public void liikuTahan(Point2D.Float p){
        annettuPiste = p;
        kayttajaltaKoordinaatit = true;
    }

    /** @return True jos uutta dataa on saapunut viime kyselyn jälkeen. */
    public boolean onMuuttunut() {
        return onMuuttunut;
    }
    
    @Override
    public void run() {
        long i = 0;
        while (true) {
            System.out.println("RoboID "+uusinPaketti.getId()+" "+Calendar.getInstance().getTime().toString() +
                               " kierros " + i++ + ".");
            if (!teeMittaukset()) {
                System.out.println("Mittaukset epäonnistuivat.");
            }
        }
    }

    /**
     * 
     * @param nykySijainti
     * @param kulma
     * @param etaisyydet
     * @return
     */
    protected Point2D.Float kokeileHakeaUusiMittauspiste(Point2D.Float nykySijainti, float kulma, int[] etaisyydet) {
        BTPaketti p = new BTPaketti(0);
        p.setNykySijainti(nykySijainti);
        p.setUusiSijainti(nykySijainti);
        Point2D.Float pf = new Point2D.Float();
        pf.x = nykySijainti.x + (float)Math.cos(Math.toRadians(kulma));
        pf.y = nykySijainti.y + (float)Math.sin(Math.toRadians(kulma));
        p.setMittausSuunta(pf);
        p.setEtaisyydet(etaisyydet);

        return haeUusiMittauspiste(p);
    }

    /**
     * 
     * @param paketti
     */
    protected void kokeileLisataHavainnotKarttaan(BTPaketti paketti) {
        lisaaHavainnotKarttaan(paketti);
    }
    
    /**
     * Lisää robotin mittaustulokset viivoina sen muistiin/karttaan.
     * 
     * Mittaustuloksia käännetään katsomisasteen verran. Tällöin karttaan
     * tulee tallennetuksi vain pysty- ja vaakasuoria janoja. Tämä helpottaa
     * datan jatkokäsittelyä.
     * 
     * @param nykySijainti Robotin nykyinen sijainti
     * @param kulma Robotin katselusuunta radiaaneina yksikköympyräin mukaisesti.
     * @param etaisyydet Robotin mittamat etäisyydet.
     */
    private void lisaaHavainnotKarttaan(BTPaketti paketti) {
        int maara = paketti.getEtaisyydet().length;
        JsimRoboNakyma nakyma = new JsimRoboNakyma(paketti.getNykySijainti(),
                (float)Math.toDegrees(paketti.getMittausKulma()), maara, 1);
        final Line2D.Float[] sateet = nakyma.getNakotaulu();
        final int[] etaisyydet = paketti.getEtaisyydet();
        
        Point2D.Float nykysijainti = paketti.getNykySijainti();
        Point2D.Float nakoP0 = nykysijainti; // Vastapäivään PI/2.
        nakoP0.x -= paketti.getMittausSuunta().y;
        nakoP0.y += paketti.getMittausSuunta().x;
        Point2D.Float nakoP1 = nykysijainti; // Myötäpäivään PI/2.
        nakoP1.x += paketti.getMittausSuunta().y;
        nakoP1.y -= paketti.getMittausSuunta().x;
        Line2D.Float nakoRaja = new Line2D.Float(nakoP0, nakoP1);

        // Laske keskimääräinen etäisyys tällä mittauskierroksella.
        double KA_ETAISYYS = 0;
        for (int i : etaisyydet)
            KA_ETAISYYS += i;
        KA_ETAISYYS /= 3*maara/2; // Kalibrointia: etäisyydestä osa pois.
        
        // Poista havaintoalueen vanhat pisteet ja leikkaa sinne jatkuvat janat.
        // Tämä on hyvin hyvin tärkeä vaihe, koska kun robotti vahingossa
        // havaitsee jotain väärin esimerkiksi luullessaan toista robottia
        // seinäksi, niin pyyhkimällä sellaiset vanhat mittaukset pois pystytään
        // päivittämään kartta aina viimeisimpien havaintojen mukaiseksi!
        for (int i = 0; i < kartta.size(); ++i) {
            boolean P0Vasemmalla = onVasemmallaPuolella(nakoRaja,
                (Point2D.Float)kartta.get(i).getP1());
            boolean P1Vasemmalla = onVasemmallaPuolella(nakoRaja,
                (Point2D.Float)kartta.get(i).getP2());

            if (P0Vasemmalla == false && P1Vasemmalla == false)
                continue; // Väärällä puolella robottia.

            if (nakoRaja.ptSegDist(nykysijainti) >= KA_ETAISYYS)
                continue; // Jana ei leikkaa näköaluetta missään kohtaa.

            double dist0 = nykysijainti.distance(kartta.get(i).getP1());
            double dist1 = nykysijainti.distance(kartta.get(i).getP2());
            
            // Jos piste on robotin takana, ei etäisyydellä ole väliä.
            if (P0Vasemmalla == false) dist0 = KA_ETAISYYS * 2;
            if (P1Vasemmalla == false) dist1 = KA_ETAISYYS * 2;
            
            if (dist0 >= KA_ETAISYYS && dist1 >= KA_ETAISYYS)
                continue; // Jos molemmat pisteet liian kaukana -> anna olla.
                          // Tässä toteutuksessa on virhe:
                          // Jos janan molemmat pisteet ovat näkökentän 
                          // ulkopuolella, mutta jana leikkaa näkökentän 
                          // jostakin keskikohdasta, ei janaa leikata lainkaan.

            // Jos jana on kokonaan uuden näkökentän sisällä niin poista se.
            if (dist0 < KA_ETAISYYS && dist1 < KA_ETAISYYS) {
                kartta.remove(i--);
                continue;
            }

            // VAIN janan TOINEN piste on näkökentässä: etsi leikkauspiste.
            float x0 = kartta.get(i).x1;
            float y0 = kartta.get(i).y1;
            float x1 = kartta.get(i).x2;
            float y1 = kartta.get(i).y2;

            // Jos alkupiste ei olekaan sisällä, niin tee vaihdos.
            if (dist0 > KA_ETAISYYS) {
                x0 = kartta.get(i).x2;
                y0 = kartta.get(i).y2;
                x1 = kartta.get(i).x1;
                y1 = kartta.get(i).y1;
            }

            // Nyt alkupiste on borottia lähinnä oleva piste ja loppupiste 
            // robotista kauempi piste. Alusta binäärihaun muuttujat.
            float dx = x1 - x0;
            float dy = y1 - y0;
            float s = 0.5f;
            float d = 0.25f;
            double etaisyys = 0;
            
            // Etsi leikkauskohta janan alku- ja loppupisteiden välillä
            // käyttämällä binääristä hakua.
            do {
                etaisyys = nykysijainti.distance(new Point2D.Float(
                    x0 + dx * s, y0 + dy * s));

                s += (etaisyys > KA_ETAISYYS ? -d : d);
                d *= 0.5f;
            } while (Math.abs(etaisyys - KA_ETAISYYS) > 0.1);
            
            // Kas niin. Uusi alkupite on löytynyt. Talleta se janaan.
            x0 += dx * s;
            y0 += dy * s;
            if (dist0 < KA_ETAISYYS) {
                kartta.get(i).x1 = x0;
                kartta.get(i).y1 = y0;
            }
            else {
                kartta.get(i).x1 = x0;
                kartta.get(i).y1 = y0;
            }
        }

        // Ota erikseen robotin havaitsemat esteet PISTEINÄ.
        ArrayList<Point2D.Float> pisteet = new ArrayList<Point2D.Float>();
        for (int i = 0; i < maara; ++i)
            if (etaisyydet[i] < maxEtaisyys) {
                sateet[i].x2 += (sateet[i].x2 - sateet[i].x1) * etaisyydet[i];
                sateet[i].y2 += (sateet[i].y2 - sateet[i].y1) * etaisyydet[i];
                boolean ainutlaatuinen = true;
                
                // Tutki onko piste jo osa jotakin kartan pistettä/janaa.
                for (Line2D.Float l : kartta)
                    if (l.ptSegDistSq(sateet[i].getP2()) < 0.001) {
                        ainutlaatuinen = false;
                        break;
                    }
                
                if (ainutlaatuinen)
                    pisteet.add((Point2D.Float) sateet[i].getP2());
            }
        
        // HUOM! Maksimi mittausetäisyys puukotettu koodiin! Kovaa koodaamista!
        final double maksimiEtaisyys = Math.sqrt(maxEtaisyys * maxEtaisyys + 
            maxEtaisyys * maxEtaisyys - 2 * maxEtaisyys * maxEtaisyys *
            Math.cos(Math.PI/(paketti.getEtaisyydet().length - 1)));

        // Yhdistä vastikään lisätyistä pisteistä kolmen perättäisen pisteen 
        // kautta kulkevalle suoralle osuvat pisteet janoiksi. Tämä poistaa
        // ylimääräiset välipisteet ja luo karttaa seinäpätkiä nopeasti.
        // HUOMIO: seinäjanat on luotava oikealta (eli listan lopusta)
        //         vasemmalle (eli listan alkuun) päin, koska Kokoajan
        //         asetaKartta-metodissa määritellään, että seinän sisäpuoli
        //         jää seinäjanan oikealle puolelle!
        while (!pisteet.isEmpty()) {
            int i = pisteet.size()-1;
            Line2D.Float l = new Line2D.Float(pisteet.get(i),
                                              pisteet.get(i));
            ArrayList<Point2D.Float> sulautetutPisteet = new 
                    ArrayList<Point2D.Float>();
            
            pisteet.remove(i);
            while (!pisteet.isEmpty()) {
                    i = pisteet.size() - 1;
                    Point2D p0 = l.getP1();
                    Point2D p1 = l.getP2();
                    Point2D p2 = pisteet.get(i);
                    
                    // Jos naapuripisteeseen on matkaa enemmän kuin
                    // maksiminäköetäisyyden päässä olevien mittauspisteiden
                    // maksimietäisyys, niin pisteet eivät varmasti ole osa
                    // samaa seinää.
                    if (p1.distance(p2) > maksimiEtaisyys)
                        break;
                    
                    Line2D.Float koeSeina = new Line2D.Float(p0, p2);

                    // Jos mikään aiemmin lisätyistä pisteistä on liian kaukana
                    // uutta seinää kuvaavasta janasta, niin älä lisää uutta
                    // pistettä seinään.
                    boolean liianKaukana = false;
                    for (Point2D.Float px : sulautetutPisteet) {
                        double etaisyys = koeSeina.ptSegDistSq(px);
                        liianKaukana = etaisyys > 0.15;
                    }

                    if (liianKaukana)
                        break;
                    
                    // Lisää piste viivan jatkoksi ja "siirrä" se kokeiltavien
                    // pisteiden listasta sulautettujen pisteiden listaan.
                    l.x2 = (float)p2.getX();
                    l.y2 = (float)p2.getY();
                    pisteet.remove(i);
                    sulautetutPisteet.add((Point2D.Float)p1);
            }

            kartta.add(l); // Lisää lopullinen seinä karttaan.
        }
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
        BTPaketti vastaus = bt.lahetaJaVastaanota(uusinPaketti, odotusMs);
        if (vastaus == null)
            return false;
        else
            uusinPaketti = vastaus;  //Talleta uusimmat tulokset
        
        // Laske robotin sijainti kartan datan perusteella.
        //uusinPaketti.setNykySijainti(arvioiTodellinenSijainti(uusinPaketti));

        // Lisää robotin havaitsemat esteet karttaan.
        lisaaHavainnotKarttaan(uusinPaketti);

        // Laske robotille uusi mittauspiste.
        Point2D.Float nykySijainti = uusinPaketti.getNykySijainti();
        Point2D.Float uusiSijainti = haeUusiMittauspiste(uusinPaketti);
        uusinPaketti.setUusiSijainti(uusiSijainti);
        
        if (uusiSijainti.x < 0 ||
            uusiSijainti.y < 0 ||
            uusiSijainti.x > 1350 ||
            uusiSijainti.y > 2000)
            throw new RuntimeException("Robotti haluaa mennä reunojen yli: " +
                    nykySijainti.x + ", " + nykySijainti.y + " -> " +
                    uusiSijainti.x + ", " + uusiSijainti.y);
        
        // Robotin uusi mittaussuunta on sama kuin suunta, johon robotti
        // kulkee nykypisteestä uuteen mittauspisteeseensä.
        float dx = uusiSijainti.x - nykySijainti.x;
        float dy = uusiSijainti.y - nykySijainti.y;
        uusiSijainti.x += dx;
        uusiSijainti.y += dy;
        uusinPaketti.setMittausSuunta(uusiSijainti);

        return onMuuttunut = true;        
    }

    /**
     * Määrittää onko piste janan vasemmalla puolella.
     * 
     * Janan vasen puoli on se puoli, joka jää vasemman käden puolelle
     * katsottaessa janaa piton sen alkupisteestä loppupisteeseen.
     * Idea on kopioitu suoraan osoitteesta
     * http://stackoverflow.com/questions/3461453/determine-which-side-of-a-line-a-point-lies
     * 
     * @param nakoRaja Jana, jonka toisella puolella piste on.
     * @param p1 Viivaa vasten testattava piste.
     * @return Tosi, jos piste on janan päällä tai vasemmalla puolella.
     */
    private boolean onVasemmallaPuolella(Line2D.Float linja, Point2D.Float c) {
        Point2D.Float a = (Point2D.Float) linja.getP1(); // Alkupiste.
        Point2D.Float b = (Point2D.Float) linja.getP2(); // Loppupiste.
        return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) >= 0;
    }

    /**
     * Tarkistaa robotin mittaustuloksista, missä päin karttaa se oikesti on.
     *
     * Robotin sijainti voi erilaisista syistä, kuten kitkan määrän mutoksista
     * tai sensorien epätarkkuudesta, johtuen heittää siitä, missä se oikeasti
     * kartalla on. Metodi tutkii robotin ilmoittamaa, antureidensa perusteella
     * todelliseksi arvioimaansa sijaintia ja sen lähiympäristöä määrittäen
     * robotin laskennallisesti todelliseksi sijainniksi sen, joka vastaa
     * historian saatussa kertyneitä mittaustuloksia parhainten.
     * 
     * HUOMAA, että metodi edellyttää toimiakseen sen, että robotti mittaa aina
     * jonkin verran myös vanhaa aluetta kartoittaessaan uutta! Jos robotti
     * kartoittaa aina 100% uutta aluetta, ei vanhan datan perusteella enää
     * pystytä määrittämään sijaintia ja koko järjestelmän oikeellisuus nojaa
     * robotin antureiden virheettömyyteen.
     * 
     * @param paketti Viimeksi saatu paketti, joka sis. robotin todell. tiedot.
     * @return Robotin todellisen sijainnin mittaamallaan kartalla.
     */
    private Point2D.Float arvioiTodellinenSijainti(BTPaketti paketti) {
        final Point2D.Float alkuperNykySijainti = paketti.getNykySijainti();
        final Point2D.Float alkuperMittausSuunta = paketti.getMittausSuunta();
        Point2D.Float tarkinNykySijainti = alkuperNykySijainti;
        Point2D.Float nykySijainti = alkuperNykySijainti;
        Point2D.Float mittausSuunta = alkuperMittausSuunta;
        final int[] alkuperEtaisyydet = paketti.getEtaisyydet();
        final int mittausMaara = alkuperEtaisyydet.length;
        
        for (int i = 0; i < mittausMaara; ++i)
            if (alkuperEtaisyydet[i] < maxEtaisyys)
                break;
            else if (i == mittausMaara - 1)
                return tarkinNykySijainti; // Jos nähtiin vain tyhjää, niin
                                           // jätä kokeilut tekemättä.
        
        JsimRobo simulaattori = new JsimRobo();
        simulaattori.setKartta(kartta.toArray(new Line2D.Float[kartta.size()]));

        float pieninVirhe = Float.MAX_VALUE;
        int kierrokset = 100;
        
        do {
            simulaattori.setPaikka(nykySijainti);
            simulaattori.käännyKohti(mittausSuunta);
            float[] tulokset = simulaattori.mittaa(mittausMaara);
            float virhe = 0;
            
            // Vertaa robotin mittaustuloksia simuloituihin mitaustuloksiin ja
            // laske niiden virheiden neliön summa.
            for (int i = 0; i < tulokset.length; ++i)
                // Jätä laskuista pois tulokset, joissa kartassa on tyhjää.
                // Kun robotti mittaa uusia alueita ja löytää sieltä UUSIA
                // esteitä, kasvaisi mittausvirhe aivan suunnattomaksi jos
                // verrattaisiin vanhan kartan tyhjiä kohtia uusien mittaus-
                // tulosten ei-tyhjiin kohtiin!
                if (tulokset[i] < maxEtaisyys) {
                    float ero = alkuperEtaisyydet[i] - tulokset[i];
                    virhe += ero * ero;
                }

            if (virhe < pieninVirhe) {
                // Löytyi piste, jossa robotti todennäköisemmin VANHOJEN
                // mitaustulosten perusteella sijaitsee. Talleta se.
                tarkinNykySijainti = nykySijainti;
                pieninVirhe = virhe;
            }

            // Arvo uusi sijainti ja mittaussuunta siirtämällä robotti
            // sattumanvaraisesti nykysijainnin lähiympäristöön. Siirrä
            // katsomispistettä samoin.
            nykySijainti = new Point2D.Float(
                    alkuperNykySijainti.x - 10 + (float)Math.random() * 20,
                    alkuperNykySijainti.y - 10 + (float)Math.random() * 20);

            // Puukotetaan mittaussuunnan muutos pois toistaiseksi.
            mittausSuunta = new Point2D.Float(
                    alkuperMittausSuunta.x - 0 + (float)Math.random() * 0,
                    alkuperMittausSuunta.y - 0 + (float)Math.random() * 0);            
        } while (--kierrokset > 0);

        return tarkinNykySijainti;
    }

    private Point2D.Float haeUusiMittauspiste(BTPaketti p) {
        return suunnistin.haeUusiSattumanvarainenMittauspiste(p);
    }
}