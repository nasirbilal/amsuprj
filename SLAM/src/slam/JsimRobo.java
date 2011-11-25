package slam;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio & arska
 */
public class JsimRobo {

    private int id;                         /// Robotin yksilöllinen tunnus.
    private float suunta;                   /// Robotin suunta, range: 0-359, jossa 0 ON POHJOINEN
    private Point2D.Float paikka;           /// Robotin paikka Point-oliona MILLIMETREISSÄ
    private JsimData mittaus;               /// luodaan mittaa()-metodilla, käytetään seuraavan mittauspaikan valitsemiseksi
    private JsimRoboNakyma nakyma;          /// luodaan mittaa()-metodilla, debugausta
    private boolean edettytäyteen = false;  ///(navigointiin) Jos (false)-> edetään täyteen näkymään, =true; jos (true) -> käännytään 180, =false.

    /// Kartta maailmasta, jossa robotti kulkee. 
    private final Rectangle      reunat =  new Rectangle(0, 0, 135, 200);                        
    private final Line2D.Float[] kartta = {new Line2D.Float(44,  0, 44, 75),
                                           new Line2D.Float(44, 75, 87, 75),
                                           new Line2D.Float(87, 75, 87, 55),
                                           new Line2D.Float(87, 55, 64, 55),
                                           new Line2D.Float(64, 55, 64,  0),
                                           new Line2D.Float(64,  0, 44,  0),

                                           new Line2D.Float(44, 112, 44, 137),
                                           new Line2D.Float(44, 137, 67, 137),
                                           new Line2D.Float(67, 137, 67, 161),
                                           new Line2D.Float(67, 161, 99, 161),
                                           new Line2D.Float(99, 161, 99, 152),
                                           new Line2D.Float(99, 152, 76, 152),
                                           new Line2D.Float(76, 152, 76, 127),
                                           new Line2D.Float(76, 127, 52, 127),
                                           new Line2D.Float(52, 127, 52, 112),
                                           new Line2D.Float(52, 112, 44, 112)};
    private final int infraKantama;   /// Robotin infrapunasensorin kantama MILLIMETREISSÄ
    private static int id_count;      /// Robotin yksilötunnuksen juokseva numero.

    public JsimRobo() {
        this(0, 800);
    }

    public JsimRobo(float suunta, int infraKantama) {
        id = id_count++;
        this.infraKantama = infraKantama;
        this.suunta = suunta;

        Point2D.Float p = new Point2D.Float(0, 0);
        while (true) {
            p.x = reunat.x + (int)(reunat.width *Math.random());
            p.y = reunat.y + (int)(reunat.height*Math.random());

            // Create a line from the point to the left
            Line2D.Float l = new Line2D.Float(p.x, p.y, p.x - reunat.width, p.y);

            int count = 0;
            for (Line2D.Float kl : kartta) // Count intersections
              count += l.intersectsLine(kl) ? 1 : 0;

            if (count % 2 == 1)
                break; // We are inside if the number of intersections is odd.
        }
        
        this.paikka = p;
    }

    public int getID() {
        return id;
    }

    public String getNimi() {
        return "JsimRobo[" + getID() + "]";
    }

    void setPaikka(Point2D.Float paikka) {
        this.paikka = paikka;
    }

    /**
     * Laskee uudet x- ja y-koordinaatit kun kuljtaan nykyisestä sijainnista
     * nykyiseen suuntaan annetun matkan verran.
     * 
     * @param matka Robotin kulkema etäisyys MILLIMETREISSÄ.
     * @return Robotin uusi sijainti.
     */
    private Point2D.Float etene(double matka) {
        double a = Math.toRadians(suunta);
        double x = paikka.x + matka * Math.sin(a);
        double y = paikka.y + matka * Math.cos(a);

        paikka = new Point2D.Float((float)x, (float)y);
        return paikka;
    }

    /** 
     * Siirtää robotin uuteen sijaintiin. Robotin katselusuunta on sen viivan
     * suuntainen, joka kulkee lähtöpisteen ja kohdepisteen kautta.
     *
     * @param kohde Kohteena oleva paikka.
     * @return Robotin uusi sijainti.
     * @note Robotti kääntyy pistettä kohti ja "ajaa" siihen: ei pathfindingiä.
     */
    public Point2D.Float etenePisteeseen(Point2D.Float kohde) {
        käännyKohti(kohde);
        return etene(kohde.distance(paikka));
    }

    /** 
     * @param aste Robotin kääntymä määrä väliltä -90 (vasen) ja 90 (oikea).
     * @return Robotin uusi suunta.
     */
    private float käänny(float aste) {
        suunta = (suunta + aste) % 360; /* Pakota suunta välille 0-359. */
        if (suunta < 0)
            suunta = suunta + 360;
        return suunta;
    }

    /** 
     * @param kohde Piste, jota kohti käännytään.
     * @return Robotin uusi suunta asteina "vaaka-akselista" myötäpäivään (?).
     */
    public float käännyKohti(Point2D.Float kohde) {

        /*
         * koska tan(alpha) = a/b, niin
         * alpha = tan⁻¹((x2-x1)/(y2-y1))
         * -tässähän meillä on tietenkin 360-astetta
         *  hoidettavana, mistä johtuen pakko vääntää iffeillä eri ilmansuunnat
         */

        if (kohde.x == paikka.x) {   //tähdätään y-akselin suuntaan
            if (kohde.y > paikka.y) {
                return käänny(-suunta);
            } else {
                return käänny(180 - suunta);
            }
        } else if (kohde.y == paikka.y) { //tähdätään x-akselin suuntaan
            if (kohde.x > paikka.x) {
                return käänny(90 - suunta);
            } else {
                return käänny(270 - suunta);
            }
        } else {

            float aste;

            if (kohde.x > paikka.x) {
                if (kohde.y > paikka.y) {
                    aste = (float) ((Math.atan((kohde.x - paikka.x) / (kohde.y - paikka.y))));
                    aste = (float) (aste * (180 / Math.PI)); //käännetään radiaanit asteiksi
                    return käänny(-suunta + aste);
                } else { // if (kohde.y < paikka.y){
                    aste = (float) ((Math.atan((kohde.x - paikka.x) / (kohde.y - paikka.y))));
                    aste = (float) (aste * (180 / Math.PI)); //käännetään radiaanit asteiksi
                    return käänny(-suunta + 180 + aste);
                }
            } else { // if (kohde.x < paikka.x){
                if (kohde.y > paikka.y) {
                    aste = (float) ((Math.atan((kohde.x - paikka.x) / (kohde.y - paikka.y))));
                    aste = (float) (aste * (180 / Math.PI)); //käännetään radiaanit asteiksi
                    return käänny(-suunta + aste);
                } else { // if (kohde.y < paikka.y){
                    aste = (float) ((Math.atan((kohde.x - paikka.x) / (kohde.y - paikka.y))));
                    aste = (float) (aste * (180 / Math.PI)); //käännetään radiaanit asteiksi
                    return käänny(-suunta + 180 + aste);
                }
            }
        }
    }

    public JsimData valitseUusiPiste(int mittausMaara) {
        mittaus = mittaa(mittausMaara);         //mittaus on osa näitä navigointihommia
        float mtaulu[] = mittaus.getData();     //tiedot edessäolevasta kamasta

        int tyhjyyslaskuri = 0;
        int tyhjyysalku = 0;
        int tyhjyysmuisti = 0;
        int tyhjyysalkumuisti = 0;

        for (int i = 0; i < mtaulu.length; i++) {
            if (mtaulu[i] == 9999) {                 //jos ei nähdä mitään
                if (i != 0) {                        // ja
                    if (mtaulu[i - 1] != 9999) {       //  jos edellinen näköviiva näki jotain
                        tyhjyysalku = i;            //      niin tämän tyhjyyden alkukohta on i
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

        /*
         * Jos ei mitään haivaittu missään
         *  niin edetään 65cm
         */

        if (tyhjyyslaskuri == mtaulu.length) { // ei mitään havaittu missään
            etene(650);
            //    System.out.println("ei mitään");
            return mittaus;
        } else if (tyhjyysmuisti == 0) { //kaikki havaitaittu kaikkialla
            //    System.out.print("kaikki kaikkialla, ");
            if (edettytäyteen) {     //BOOLEAN EDETTYTÄYTEEN on siirretty tämän metodin ulkopuolelle
                //        System.out.println("ETtrue");
                edettytäyteen = false;
                käänny(180);
                return mittaus;
            } else {
                //        System.out.println("ETfalse");
                etene(mtaulu[19] / 2);    // edetään suoraan eteenpäin puolet eteenpäin mitatusta pituudesta
                edettytäyteen = true;
                return mittaus;
            }
        } else {
            //    System.out.println("normi");

            //    System.out.println("käänny("+(((tyhjyysalkumuisti*5)-90)+((tyhjyysmuisti/2)*5))+")"); //debug

            käänny(((tyhjyysalkumuisti * 5) - 90) + ((tyhjyysmuisti / 2) * 5)); //witness the true power of mathematics!!!

            if ((tyhjyysalkumuisti - 1) >= 0) {
                etene((mtaulu[tyhjyysalkumuisti - 1] + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
            } else {
                etene((0 + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
            }
            return mittaus;
        }
    }

    /*
     * tämä on melkein obsolete muttei poisteta vielä, jos vaikka tarvii tulevaisuudessa
     */
    private void navSuurinVäli(float tap, int as, float tlp, int ls) {


        float x1 = (float) (paikka.x + tap * Math.sin(((suunta + (as * 5 - 90)) * (Math.PI / 180))));
        float y1 = (float) (paikka.y + tap * Math.cos(((suunta + (as * 5 - 90)) * (Math.PI / 180))));

        float x2 = (float) (paikka.x + tlp * Math.sin(((suunta + (ls * 5 - 90)) * (Math.PI / 180))));
        float y2 = (float) (paikka.y + tlp * Math.cos(((suunta + (ls * 5 - 90)) * (Math.PI / 180))));


        System.out.println("(" + x1 + "," + y1 + ") - (" + x2 + "," + y2 + ")");    //debug

        float keskipisteX = (x1 + x2) / 2;
        float keskipisteY = (y1 + y2) / 2;

        System.out.println("keskipiste:(" + keskipisteX + "," + keskipisteY + ")"); //debug

        Point2D.Float etenemiskohde = new Point2D.Float(keskipisteX, keskipisteY);

        System.out.println("etenepisteeseen(" + etenemiskohde.x + "," + etenemiskohde.y + ")");
        System.out.println("1jantusen sijainti nyt:(" + etenemiskohde.x + "," + etenemiskohde.y + ")");
        System.out.println("1jantusen suunta nyt:" + suunta);

        etenePisteeseen(etenemiskohde);

        System.out.println("2jantusen sijainti nyt:(" + etenemiskohde.x + "," + etenemiskohde.y + ")");
        System.out.println("2jantusen suunta nyt:" + suunta);

        käännyKohti(new Point2D.Float(x1, y1));

        System.out.println("3jantusen sijainti nyt:(" + etenemiskohde.x + "," + etenemiskohde.y + ")");
        System.out.println("3jantusen suunta nyt:" + suunta);
    }

    /*
     * Mittaus
     */
    public JsimData mittaa(int mittausMaara) {
        /*
         * Tässä olis ideana, että verrataan robotin yhtä "näköviivaa" kaikkiin kartan viivoihin vuoron perään ja jos leikkaus
         * löytyy niin tallennetaan se pieninleikkaus-muuttujaan, jota vertaillaan tuleviin leikkauspituuksiin. Sitten tallennetaan
         * pieninleikkaus tauluun ja siirrytään tarkastelemaan seuraavaa näköviivaa. Lopuksi pieninleikkaus-muuttujista muodostettu
         * taulu annetaan JsimData oliolle, johon myös tallennetaan robotin tämänhetkinen suunta.
         */

        /*
         * jos tää roska antaa 9999 pituudeksi niin se tarkoittaa että mitää ei ole havaittu
         * 
         */

        //JsimData mittaus; !!siirretty ylös!!
        //JsimRoboNäkymä siirretty ylös
        float taulu[] = new float[mittausMaara];    //Käytetään "mittaus"-jsimdatan luomisessa
        float pieninleikkaus;
        nakyma = new JsimRoboNakyma(paikka, suunta, mittausMaara, infraKantama);

        //System.out.println("luuppaus alkaa");//debug

        for (int i = 0; i < nakyma.getNakotaulu().length; i++) { //iteroidaan JsimRoboNäkymän Näkötaulun näköviivoja
            pieninleikkaus = 9999; //jos mikään ei leikkaa annetaan arvo 9999
            for (int k = 0; k < kartta.length; k++) { //iteroidaan kartan viivoja
                Point2D.Float leikkauspiste = nakyma.laskeLeikkauspiste(nakyma.getNakotaulu()[i], kartta[k]); //pistetään leikkauspiste muistiin
                if (leikkauspiste != null) {
                    if (pieninleikkaus > Math.sqrt(Math.pow(paikka.x - leikkauspiste.x, 2) + (Math.pow(paikka.y - leikkauspiste.y, 2)))) {
                        //jos leikkauspiste on pienempi kuin muistissa oleva lyhyin matka leikkauspisteeseen
                        pieninleikkaus = (float) Math.sqrt(Math.pow(paikka.x - leikkauspiste.x, 2) + (Math.pow(paikka.y - leikkauspiste.y, 2)));
                        //overwritataan wanha kaukaisempi leikkauspiste uudella lyhyemmällä
                        //                System.out.println("pit:"+pieninleikkaus);
                    }

                }
            }
            taulu[i] = pieninleikkaus; // pistetään lyhyin etäisyys muistiin
        }

        mittaus = new JsimData(suunta, taulu, paikka); //annetaan etäisyystaulukko JsimDatalle, josta se toivottavasti pistetään johonkin muistiin

        return mittaus;
    }
}
