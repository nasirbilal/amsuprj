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
    private boolean kayttajaltaKoordinaatit;
    private Point2D.Float annettuPiste;   //Käyttäjän antama piste. Jos käyttäjä antaa pisteen, sinne kuljetaan aina
    
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
        this.paketti = new BTPaketti(ID);
        this.paketti.setNykySijainti(new Point2D.Float(0, 0));
        this.paketti.setUusiSijainti(new Point2D.Float(0, 0));
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
/*
        paketti = vastaus; // Talleta uusimmat tulokset.
        float dx = paketti.getMittausSuunta().x - paketti.getNykySijainti().x;
        float dy = paketti.getMittausSuunta().y - paketti.getNykySijainti().y;
        float alpha = (float) Math.atan2(dy, dx);
        alpha += (alpha < 0 ? 2*Math.PI : 0);
        int maara = paketti.getEtaisyydet().length;
        int[] etaisyydet = paketti.getEtaisyydet();
        JsimRoboNakyma nakyma = new JsimRoboNakyma(paketti.getNykySijainti(),
                alpha, maara, 1);
        Line2D.Float[] sateet = nakyma.getNakotaulu();
*/

        
        paketti = vastaus;  //Talleta uusimmat tulokset
        
        //Lasketaan robotin suunta:
        float kulma;
        if (paketti.getMittausSuunta().y > paketti.getNykySijainti().y){ //Jos suunta on koordinaatistossa "ylöspäin":
            float dx = paketti.getMittausSuunta().x - paketti.getNykySijainti().x;
            float dy = paketti.getMittausSuunta().y - paketti.getNykySijainti().y;
            kulma = (float)(Math.atan(dx/dy)*(180/Math.PI)); //kulma asteina
            
        } else if (paketti.getMittausSuunta().y < paketti.getNykySijainti().y){ //Jos suunta on koordinaatistossa "alaspäin":
            float dx = paketti.getMittausSuunta().x - paketti.getNykySijainti().x;
            float dy = paketti.getMittausSuunta().y - paketti.getNykySijainti().y;
            kulma = (float)(Math.atan(dx/dy)*(180/Math.PI)+180); //kulma asteina
            
        } else if (paketti.getMittausSuunta().y == paketti.getNykySijainti().y){ //Jos suunta on x-akselin suuntainen:
            if (paketti.getMittausSuunta().x > paketti.getNykySijainti().x){
                kulma = 90;
            } else if (paketti.getMittausSuunta().x < paketti.getNykySijainti().x){
                kulma = 270;
            } else {
                kulma = 0;  //tilt - homma osoittaa itteensä
            }
        } else {
            kulma = 0;  //Jotain epäinitialisaatiota vingutaan
        }
        
        int maara = paketti.getEtaisyydet().length;
        int[] etaisyydet = paketti.getEtaisyydet();
        JsimRoboNakyma nakyma = new JsimRoboNakyma(paketti.getNykySijainti(),kulma, maara, 1);
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
            paketti.setUusiSijainti(menopiste);
            
            float kx = (float)(paketti.getNykySijainti().x + 800 * Math.sin(Math.toRadians(kulma)));
            float ky = (float)(paketti.getNykySijainti().y + 800 * Math.cos(Math.toRadians(kulma)));
            Point2D.Float katsepiste = new Point2D.Float(kx,ky);
            paketti.setMittausSuunta(katsepiste);
            
            
        } else if (tyhjyysmuisti == 0) { //kaikki havaitaittu kaikkialla
            if (edettytäyteen) {     
                edettytäyteen = false;
                //käänny(180);
                
                paketti.setUusiSijainti(paketti.getNykySijainti()); // ei liikettä
                
                float kx = (float)(paketti.getNykySijainti().x - 800 * Math.sin(Math.toRadians(kulma)));
                float ky = (float)(paketti.getNykySijainti().y - 800 * Math.cos(Math.toRadians(kulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                
            } else {
                //etene(etaisyydet[19] / 2);// edetään suoraan eteenpäin puolet eteenpäin mitatusta pituudesta
                
                float sx = (float)(paketti.getNykySijainti().x + (etaisyydet[19] / 2) * Math.sin(Math.toRadians(kulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
                float sy = (float)(paketti.getNykySijainti().y + (etaisyydet[19] / 2) * Math.cos(Math.toRadians(kulma)));
                Point2D.Float menopiste = new Point2D.Float(sx,sy);
                paketti.setUusiSijainti(menopiste);

                float kx = (float)(paketti.getNykySijainti().x + ((etaisyydet[19] / 2) *1.5) * Math.sin(Math.toRadians(kulma)));
                float ky = (float)(paketti.getNykySijainti().y + ((etaisyydet[19] / 2) *1.5) * Math.cos(Math.toRadians(kulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                
                edettytäyteen = true;
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
                paketti.setUusiSijainti(menopiste);

                float kx = (float)(paketti.getNykySijainti().x + (((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.sin(Math.toRadians(modkulma)));
                float ky = (float)(paketti.getNykySijainti().y + (((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
            } else {
                //etene((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
                
                float sx = (float)(paketti.getNykySijainti().x + ((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.sin(Math.toRadians(modkulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
                float sy = (float)(paketti.getNykySijainti().y + ((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float menopiste = new Point2D.Float(sx,sy);
                paketti.setUusiSijainti(menopiste);

                float kx = (float)(paketti.getNykySijainti().x + (((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.sin(Math.toRadians(modkulma)));
                float ky = (float)(paketti.getNykySijainti().y + (((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
            }
        }
        
        onMuuttunut = true;
        return true;
        
    }   
        
/*        
        // Laske robotille uusi mittauspiste ja -suunta.
        // Luo neljä sädettä robotista pääilmansuuntiin ja suunnista kohti sitä
        // loppupistettä, joka on etäämmällä robotista.
        if (!kayttajaltaKoordinaatit) {
            float x = paketti.getNykySijainti().x;
            float y = paketti.getNykySijainti().y;
            Line2D.Float[] suunnat = {
                new Line2D.Float(paketti.getNykySijainti(), new Point2D.Float(
                x, y - 800 * 100)),
                new Line2D.Float(paketti.getNykySijainti(), new Point2D.Float(
                x - 800 * 100, y)),
                new Line2D.Float(paketti.getNykySijainti(), new Point2D.Float(
                x, y + 800 * 100)),
                new Line2D.Float(paketti.getNykySijainti(), new Point2D.Float(
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
            paketti.setUusiSijainti(new Point2D.Float(x + dx, y + dy));
            
            //Toggle
            kayttajaltaKoordinaatit = false;
        }else{
            paketti.setUusiSijainti(annettuPiste);
        }
        onMuuttunut = true;
        return true;
    }
*/
    //Käyttäjä klikkaa kartalla pistettä ja käskemme robottia liikkumaan siihen pisteeseen
    //emmekä laske erikseen uutta pistettä.
    public void liikuTahan(Point2D.Float p){
        annettuPiste = p;
        kayttajaltaKoordinaatit = true;
    }
    
    
    public Point2D.Float annaKoordinaatit(){
        return paketti.getNykySijainti();
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
