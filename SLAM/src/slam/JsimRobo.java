package slam;

import java.awt.geom.Point2D;
import java.util.Random;
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
        
        boolean sisalla = false;
        do {
//            if (p.x < boundingRect.x || p.y < boundingRect.y) return false;
//            if (p.x > boundingRect.x + boundingRect.width
//                || p.y > boundingRect.y + boundingRect.height) return false;
//
//            // Create a line from the point to the left
//            Line l = new Line(p.x, p.y, p.x - boundingRect.width, p.y);
//
//            // Count intersections
//            int count = 0;
//            for (int i = 0; i < lines.length; i++) {
//              if (lines[i].intersectsAt(l) != null) count++;
//            }
//            // We are inside if the number of intersections is odd
//            return count % 2 == 1;
        } while (sisalla);
    }

    public int getID() {
        return id;
    }

    public String getNimi() {
        return "JsimRobo[" + getID() + "]";
    }

    public float getSuunta() {
        return suunta;
    }

    public void setSuunta(float suunta) {
        this.suunta = suunta;
    }

    public Point2D.Float getPaikka() {
        return paikka;
    }

    public void setPaikka(float x, float y) {
        paikka = new Point2D.Float(x, y);
    }

    void setPaikka(Point2D.Float paikka) {
        setPaikka(paikka.x, paikka.y);
    }

    /*
     * Liikuntametodit:
     */
    /**
     * Lasketaan uudet x- ja y-koordinaatit vanhojen koordinaattien
     * ja suunnan perusteella. esim.
     * (uusi X) = (vanha X) + (kuljettava matka)*sin(nykyinen suunta)
     * 
     * @param matka on MILLIMETREISSÄ.
     * @return uuden paikan Point2D.Float-oliona.
     */
    public Point2D.Float etene(float matka) {
        float x = (float) (paikka.x + matka * Math.sin((suunta * (Math.PI / 180)))); //Mathin funktiot ottaa radiaaneja
        float y = (float) (paikka.y + matka * Math.cos((suunta * (Math.PI / 180))));

        paikka = new Point2D.Float(x, y);
        return paikka;
    }

    /** 
     * Käytetään käännyKohti-metodia oikean suunnan asettamiseksi, jonka
     * jälkeen liikutaan "tangentin" pituus pythagoraan lauseeseen perustuen:
     * 
     * sqrt((x2-x1)²+(y2-y1)²)
     *
     * @param on kohteena oleva paikka.
     * @return uuden paikan Point2D.Float-oliona.
     * @note Robotti kääntyy pistettä kohti ja "ajaa" siihen. Ei mitään pathfindingiä.
     */
    public Point2D.Float etenePisteeseen(Point2D.Float kohde) {
        käännyKohti(kohde);
        return etene((float) Math.sqrt(Math.pow(kohde.x - paikka.x, 2) + (Math.pow(kohde.y - paikka.y, 2))));
    }

    /** 
     * @param aste on väliltä -90 (vasemmalle) viiva 90 (oikealle).
     * @return uuden suunnan.
     */
    public float käänny(float aste) {

        /*
         * tässä pakotetaan ensin suunnan ja kääntymisen
         * summa sallitulle alueelle (0-359)
         */

        suunta = suunta + aste;
        if (suunta > 359) {
            suunta = suunta - 360;
        } else if (suunta < 0) {
            suunta = suunta + 360;
        }
        return suunta;
    }

    /** 
     * @param Point2D.Float-olio, jota kohti käännytään.
     * @param on mahdollinen lisäkääntyminen, anna 0 jos ei tarvetta
     * @return uuden suunnan.
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
    boolean edettytäyteen = false; //(navigointiin) Jos (false)-> edetään täyteen näkymään, =true; jos (true) -> käännytään 180, =false.
    /*Tätä booleania käytetään valitseUusiPiste-metodissa.
     * Tää saattaa jotain pienimuotosia ongelmia kehittää
     * kun tää on tässä, mutta ne todennäkösesti korjautuu
     * ittestään.
     */

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

            //    System.out.println("tam:"+tyhjyysalkumuisti);       //lisää debugia
            //    if ((tyhjyysalkumuisti-1) >= 0){
            //        System.out.println("mtaulu[tam-1]=" + mtaulu[tyhjyysalkumuisti-1]);
            //    } else {
            //        System.out.println("tam-1=" + (tyhjyysalkumuisti-1));
            //    }
            //    System.out.println("tm:"+tyhjyysmuisti);
            //    System.out.println("tam+tm:"+(tyhjyysalkumuisti+tyhjyysmuisti));
            //    System.out.println("mtaulu[tam+tm]"+mtaulu[tyhjyysalkumuisti+tyhjyysmuisti]);
            //    if ((tyhjyysalkumuisti-1) >= 0){
            //        System.out.println("etene("+ ((mtaulu[tyhjyysalkumuisti-1]+mtaulu[tyhjyysalkumuisti+tyhjyysmuisti])/2) + ");"); //debug out
            //    } else {
            //        System.out.println("etene("+ ((mtaulu[tyhjyysalkumuisti+tyhjyysmuisti])/2 ) + ")");
            //    }

            if ((tyhjyysalkumuisti - 1) >= 0) {
                etene((mtaulu[tyhjyysalkumuisti - 1] + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
            } else {
                etene((0 + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
            }
            //yksinkertaistettu. tekosyy: tää kuluttaa vähemmän rosessoria
            //ja näin myös vältytään toivottavasti seiniin törmäilyltä paremmin
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
                if (nakyma.getNakoviiva(i).intersectsLine(kartta[k])) { //JOS näköviiva leikkaan karttaviivan:
                    //            System.out.print("kartta["+k+"] leikkaa näköviiva["+i+"]");
                    Point2D.Float leikkauspiste = nakyma.leikkaako(nakyma.getNakoviiva(i), kartta[k]); //pistetään leikkauspiste muistiin

                    //            System.out.print(" paikassa("+leikkauspiste.x+","+leikkauspiste.y+"), ");

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
