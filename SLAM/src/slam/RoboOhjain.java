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
        this.paketti = bt.annaOletusPaketti();
        this.roboNakyma = new Point2D.Double[BTPaketti.MAARA];
        this.kartta = new ArrayList<Line2D.Float>();

        JsimRoboNakyma nakyma = new JsimRoboNakyma(new Point2D.Float(0, 0),
                0, paketti.getEtaisyydet().length, 1);
        this.mittausJanat = nakyma.getNakotaulu();
    }

    public Point2D.Float annaKoordinaatit(){
        return paketti.getNykySijainti();
    }

    /**
     * Aseta Bluetooth-paketti ohjaimelle testausta varten.
     * @param paketti Valmiiksi alustettu paketti.
     */
    public void asetaTestausPaketti(BTPaketti paketti) {
        this.paketti = paketti;
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

        final int[] etaisyydet = paketti.getEtaisyydet();
        if (mittausJanat.length != etaisyydet.length)
            return null;
        
        for (int i = 0; i < mittausJanat.length; ++i) {
            if (etaisyydet[i] >= maxEtaisyys) {
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

    protected Point2D.Float kokeileHakeaUusiMittauspiste(Point2D.Float nykySijainti,
                                              float kulma, int[] etaisyydet) {
        return haeUusiMittauspiste(nykySijainti, kulma, etaisyydet);
    }

    protected void kokeileLisataHavainnotKarttaan(Float nykySijainti, float kulma, int[] etaisyydet) {
        lisaaHavainnotKarttaan(nykySijainti, kulma, etaisyydet);
    }
        
    private Point2D.Float haeUusiMittauspiste(Point2D.Float nykySijainti,
                                              float kulma, int[] etaisyydet) {
     //   return nykySijainti;
        int tyhjyyslaskuri = 0;
        int tyhjyysalku = 0;
        int tyhjyysmuisti = 0;
        int tyhjyysalkumuisti = 0;

        //Mittausten käsittely:
        
        for (int i = 0; i < etaisyydet.length; i++) {
            if (etaisyydet[i] > maxEtaisyys) {                 //jos ei nähdä mitään
                if (i != 0) {                                  // ja
                    if (etaisyydet[i - 1] < maxEtaisyys) {     //  jos edellinen näköviiva näki jotain
                        tyhjyysalku = i;                       //   niin tämän tyhjyyden alkukohta on i
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
            
            float sx = (float)(paketti.getNykySijainti().x + 650 * Math.sin(Math.toRadians(kulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
            float sy = (float)(paketti.getNykySijainti().y + 650 * Math.cos(Math.toRadians(kulma)));
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
                return paketti.getNykySijainti();
                
              /*  float kx = (float)(paketti.getNykySijainti().x - 800 * Math.sin(Math.toRadians(kulma)));
                float ky = (float)(paketti.getNykySijainti().y - 800 * Math.cos(Math.toRadians(kulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
               */ 
            } else {
                //etene(etaisyydet[19] / 2);// edetään suoraan eteenpäin puolet eteenpäin mitatusta pituudesta
                edettytäyteen = true;
                float sx = (float)(paketti.getNykySijainti().x + (etaisyydet[19] / 2) * Math.sin(Math.toRadians(kulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
                float sy = (float)(paketti.getNykySijainti().y + (etaisyydet[19] / 2) * Math.cos(Math.toRadians(kulma)));
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
            
            float modkulma = kulma + ((tyhjyysalkumuisti * 5) - 90) + ((tyhjyysmuisti / 2) * 5);
            
            
            if ((tyhjyysalkumuisti - 1) >= 0) {
                //etene((mtaulu[tyhjyysalkumuisti - 1] + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
                float sx = (float)(paketti.getNykySijainti().x + ((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.sin(Math.toRadians(modkulma))); //Jos ei toimijnejne...
                float sy = (float)(paketti.getNykySijainti().y + ((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.cos(Math.toRadians(modkulma)));
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
                
                float sx = (float)(paketti.getNykySijainti().x + ((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.sin(Math.toRadians(modkulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
                float sy = (float)(paketti.getNykySijainti().y + ((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.cos(Math.toRadians(modkulma)));
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

    private void lisaaHavainnotKarttaan(Float nykySijainti, float kulma, int[] etaisyydet) {
        int maara = paketti.getEtaisyydet().length;
        JsimRoboNakyma nakyma = new JsimRoboNakyma(nykySijainti, kulma, maara, 1);
        Line2D.Float[] sateet = nakyma.getNakotaulu();

        // Lisää robotin havaitsemat esteet karttaan.
        for (int i = 1; i < maara; ++i) {
            if (etaisyydet[i - 1] < maxEtaisyys && etaisyydet[i] < maxEtaisyys) {
                kartta.add(new Line2D.Float(sateet[i - 1].getP2(),
                        sateet[i].getP2()));
            }
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
        BTPaketti vastaus = bt.lahetaJaVastaanota(paketti, odotusMs);
        if (vastaus == null)
            return false;
        else
            paketti = vastaus;  //Talleta uusimmat tulokset
        
        float dx = paketti.getMittausSuunta().x - paketti.getNykySijainti().x;
        float dy = paketti.getMittausSuunta().y - paketti.getNykySijainti().y;
        float kulma = (float) Math.atan2(dy, dx);
        kulma += (kulma < 0 ? 2*Math.PI : 0);

        // Lisää robotin havaitsemat esteet karttaan.
        lisaaHavainnotKarttaan(paketti.getNykySijainti(), kulma,
                               paketti.getEtaisyydet());

        // Laske robotin sijainti kartan datan perusteella.
        // TODO.


        // Laske robotille uusi mittauspiste.
        paketti.setUusiSijainti(haeUusiMittauspiste(paketti.getNykySijainti(),
                kulma, paketti.getEtaisyydet()));
        
        // Robotin uusi mittaussuunta on sama kuin suunta, johon robotti
        // kulkee nykypisteestä uuteen mittauspisteeseensä.
        dx = paketti.getUusiSijainti().x - paketti.getNykySijainti().x;
        dy = paketti.getUusiSijainti().y - paketti.getNykySijainti().y;
        paketti.setMittausSuunta(paketti.getUusiSijainti());
        paketti.getMittausSuunta().x += dx;
        paketti.getMittausSuunta().x += dy;

        return onMuuttunut = true;        
    }   
}
