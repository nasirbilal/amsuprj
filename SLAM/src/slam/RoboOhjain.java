/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
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
    
    boolean edettytäyteen;  //Käytetään navigointiin

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

        JsimRoboNakyma nakyma = new JsimRoboNakyma(new Point2D.Float(0, 0),
                0, uusinPaketti.getEtaisyydet().length, 1);
        this.mittausJanat = nakyma.getNakotaulu();
    }

    public Point2D.Float annaKoordinaatit(){
        return uusinPaketti.getNykySijainti();
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
                roboNakyma[i].y = mittausJanat[i].x2 * etaisyydet[i];
                roboNakyma[i].x = mittausJanat[i].y2 * etaisyydet[i];
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
    
    //Käyttäjä klikkaa kartalla pistettä ja käskemme robottia liikkumaan siihen pisteeseen
    //emmekä laske erikseen uutta pistettä.
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
            System.out.println(Calendar.getInstance().getTime().toString() +
                               " kierros " + i++ + ".");
            if (!teeMittaukset()) {
                System.out.println("Mittaukset epäonnistuivat.");
            }
        }
    }

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

    protected void kokeileLisataHavainnotKarttaan(BTPaketti paketti) {
        lisaaHavainnotKarttaan(paketti);
    }
        
    private Point2D.Float haeUusiMittauspiste(BTPaketti paketti) {
        Point2D.Float nykySijainti = paketti.getNykySijainti();
        float kulma = (float) Math.toDegrees(paketti.getMittausKulma());
        int[] etaisyydet = paketti.getEtaisyydet();
        int tyhjyyslaskuri = 0;
        int tyhjyysalku = 0;
        int tyhjyysmuisti = 0;
        int tyhjyysalkumuisti = 0;
        
        if (Math.random() < 2.0) // Tämä on tässä kunnes testit menee läpi!
            return haeUusiSattumanvarainenMittauspiste(paketti);
        
        for (int i = 0; i < etaisyydet.length; i++) {
            if (etaisyydet[i] >= maxEtaisyys) {                // jos ei nähdä mitään
                if (i != 0) {                                  // ja
                    if (etaisyydet[i - 1] < maxEtaisyys) {     // jos edellinen näköviiva näki jotain
                        tyhjyysalku = i;                       // niin tämän tyhjyyden alkukohta on i
                    }
                } else {
                    tyhjyysalku = 0;
                }
                tyhjyyslaskuri++;
            } else {
                if (tyhjyyslaskuri > tyhjyysmuisti) {
                    tyhjyysmuisti = tyhjyyslaskuri;
                    tyhjyysalkumuisti = tyhjyysalku;
                }
                tyhjyyslaskuri = 0;
            }
        }
        
        //Navigoinnin valinta:
        
        if (tyhjyyslaskuri == etaisyydet.length) { // ei mitään havaittu missään
            //etene(650);
            
            float sx = (float)(nykySijainti.x + 650 * Math.sin(Math.toRadians(kulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
            float sy = (float)(nykySijainti.y + 650 * Math.cos(Math.toRadians(kulma)));
            Point2D.Float menopiste = new Point2D.Float(sx,sy);
            //paketti.setUusiSijainti(menopiste);
            return menopiste;
            
          /*  float kx = (float)(paketti.getNykySijainti().x + 800 * Math.sin(Math.toRadians(kulma)));
            float ky = (float)(paketti.getNykySijainti().y + 800 * Math.cos(Math.toRadians(kulma)));
            Point2D.Float katsepiste = new Point2D.Float(kx,ky);
            paketti.setMittausSuunta(katsepiste);
          */  
            
        } else if (tyhjyysmuisti == 0) { //kaikki havaitaittu kaikkialla
            if (edettytäyteen) {     
                edettytäyteen = false;
                //käänny(180);
                
                //paketti.setUusiSijainti(paketti.getNykySijainti()); // ei liikettä
                return nykySijainti;
                
              /*  float kx = (float)(paketti.getNykySijainti().x - 800 * Math.sin(Math.toRadians(kulma)));
                float ky = (float)(paketti.getNykySijainti().y - 800 * Math.cos(Math.toRadians(kulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
               */ 
            } else {
                //etene(etaisyydet[19] / 2);// edetään suoraan eteenpäin puolet eteenpäin mitatusta pituudesta
                edettytäyteen = true;
                float sx = (float)(nykySijainti.x + (etaisyydet[19] / 2) * Math.sin(Math.toRadians(kulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
                float sy = (float)(nykySijainti.y + (etaisyydet[19] / 2) * Math.cos(Math.toRadians(kulma)));
                Point2D.Float menopiste = new Point2D.Float(sx,sy);
                //paketti.setUusiSijainti(menopiste);
                 
                return menopiste;
                
                /*float kx = (float)(paketti.getNykySijainti().x + ((etaisyydet[19] / 2) *1.5) * Math.sin(Math.toRadians(kulma)));
                float ky = (float)(paketti.getNykySijainti().y + ((etaisyydet[19] / 2) *1.5) * Math.cos(Math.toRadians(kulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                */
                
            }
        } else {    //normiliikettä eli hidasta batistiinikävelyä
            //käänny(((tyhjyysalkumuisti * 5) - 90) + ((tyhjyysmuisti / 2) * 5));
            
            //kuinka paljon nyt tahdotaan kääntyä?

            float modkulma = kulma + ((tyhjyysalkumuisti * 5) - 90) + (((float)tyhjyysmuisti / 2) * 5);
            
            
            if ((tyhjyysalkumuisti - 1) >= 0) {
                
                //etene((mtaulu[tyhjyysalkumuisti - 1] + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
                float sx = (float)(nykySijainti.x + ((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.sin(Math.toRadians(modkulma))); //Jos ei toimijnejne...
                float sy = (float)(nykySijainti.y + ((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float menopiste = new Point2D.Float(sx,sy);
                //paketti.setUusiSijainti(menopiste);
                return menopiste;
                
                
                /* kx = (float)(paketti.getNykySijainti().x + (((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.sin(Math.toRadians(modkulma)));
                float ky = (float)(paketti.getNykySijainti().y + (((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                */
            } else {
                //etene((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
                float sx = (float)(nykySijainti.x + ((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.sin(Math.toRadians(modkulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
                float sy = (float)(nykySijainti.y + ((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float menopiste = new Point2D.Float(sx,sy);
                //paketti.setUusiSijainti(menopiste);
                
                return menopiste;

                /*float kx = (float)(paketti.getNykySijainti().x + (((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.sin(Math.toRadians(modkulma)));
                float ky = (float)(paketti.getNykySijainti().y + (((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                */
            }
        }
    }

    private Point2D.Float haeUusiSattumanvarainenMittauspiste(BTPaketti paketti) {
        final int[] etaisyydet = paketti.getEtaisyydet();
        int[] pisimmat = new int[etaisyydet.length];
        int numPisimmat = 0;
        int pisin = 0;
       
        if (Math.random() < 2.0) // ANKARAA PUUKOTUSTA!
            return new Point2D.Float((float)Math.random() * 1350, (float)Math.random() * 2000);
        
        // Etsi pisin näköviiva.
        for (int i = 1; i < etaisyydet.length; ++i)
            if (etaisyydet[i] > pisin)
                pisin = etaisyydet[i];
       
        // Lisää kaikki pisimmät näköviivat listaan.
        for (int i = 0; i < etaisyydet.length; ++i)
            if (etaisyydet[i] == pisin)
                pisimmat[numPisimmat++] = i;
        
        // Valitse pisin näköviiva sattumanvaraisesti.
        int x = (int) (Math.random() * numPisimmat);
        JsimRoboNakyma nakyma = new JsimRoboNakyma(paketti.getNykySijainti(),
            (float)paketti.getMittausKulma(), etaisyydet.length, pisin);
        Line2D.Float jana = nakyma.getNakotaulu()[pisimmat[x]];

        // Liiku näköviivaa pitkin. Älä kuitenkaan mene aivan loppuun asti,
        // jotta ei päädyttäisi jonkin seinän sisään.
        jana.x2 -= (jana.x2 - jana.x1) * 0.05;
        jana.y2 -= (jana.y2 - jana.y1) * 0.05;
        
        return (Point2D.Float) jana.getP2();
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
                (float)paketti.getMittausKulma(), maara, 1);
        final Line2D.Float[] sateet = nakyma.getNakotaulu();
        final int[] etaisyydet = paketti.getEtaisyydet();
        
        // Lisää robotin havaitsemat esteet karttaan.
        sateet[0].x2 += (sateet[0].x2 - sateet[0].x1) * etaisyydet[0];
        sateet[0].y2 += (sateet[0].y2 - sateet[0].y1) * etaisyydet[0];
        if (etaisyydet[0] < maxEtaisyys)
            kartta.add(new Line2D.Float(sateet[0].getP2(), sateet[0].getP2()));
        
        for (int i = 1; i < maara; ++i) {
            sateet[i].x2 += (sateet[i].x2 - sateet[i].x1) * etaisyydet[i];
            sateet[i].y2 += (sateet[i].y2 - sateet[i].y1) * etaisyydet[i];
            
            if (etaisyydet[i] >= maxEtaisyys)
                continue;
            
            if (etaisyydet[i - 1] < maxEtaisyys &&
               (Math.abs(sateet[i-1].x2-sateet[i].x2) < 0.001 ||
                Math.abs(sateet[i-1].y2-sateet[i].y2) < 0.001)) {
                kartta.add(new Line2D.Float(sateet[i - 1].getP2(),
                        sateet[i].getP2()));
            }
            else
                kartta.add(new Line2D.Float(sateet[i].getP2(), sateet[i].getP2()));
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
        
        // Lisää robotin havaitsemat esteet karttaan.
        lisaaHavainnotKarttaan(uusinPaketti);

        // Laske robotin sijainti kartan datan perusteella.
        // TODO.


        // Laske robotille uusi mittauspiste.
        Point2D.Float uusiSijainti = haeUusiMittauspiste(uusinPaketti);
        Point2D.Float nykySijainti = uusinPaketti.getNykySijainti();
        uusinPaketti.setUusiSijainti(uusiSijainti);
        
        if (uusiSijainti.x < 0 ||
            uusiSijainti.y < 0 ||
            uusiSijainti.x > 1350 ||
            uusiSijainti.y > 2000)
            throw new RuntimeException("Robotti haluaa mennä reunojen yli: " +
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
}
