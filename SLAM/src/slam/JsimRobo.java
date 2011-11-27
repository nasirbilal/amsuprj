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
    private JsimRoboNakyma nakyma;          /// luodaan mittaa()-metodilla, debugausta
    private boolean edettytäyteen = false;  ///(navigointiin) Jos (false)-> edetään täyteen näkymään, =true; jos (true) -> käännytään 180, =false.

    /// Kartta maailmasta, jossa robotti kulkee. 
    private final Rectangle      reunat =  new Rectangle(0, 0, 1350, 2000);                        
    private final Line2D.Float[] kartta = {new Line2D.Float(440,   0, 440, 750),
                                           new Line2D.Float(440, 750, 870, 750),
                                           new Line2D.Float(870, 750, 870, 550),
                                           new Line2D.Float(870, 550, 640, 550),
                                           new Line2D.Float(640, 550, 640,   0),
                                           new Line2D.Float(640,   0, 440,   0),

                                           new Line2D.Float(440, 1120, 440, 1370),
                                           new Line2D.Float(440, 1370, 670, 1370),
                                           new Line2D.Float(670, 1370, 670, 1610),
                                           new Line2D.Float(670, 1610, 990, 1610),
                                           new Line2D.Float(990, 1610, 990, 1520),
                                           new Line2D.Float(990, 1520, 760, 1520),
                                           new Line2D.Float(760, 1520, 760, 1270),
                                           new Line2D.Float(760, 1270, 520, 1270),
                                           new Line2D.Float(520, 1270, 520, 1120),
                                           new Line2D.Float(520, 1120, 440, 1120),
    
                                           new Line2D.Float(0,    0,    0,    2000),
                                           new Line2D.Float(0,    2000, 1350, 2000),
                                           new Line2D.Float(1350, 2000, 1350, 0),
                                           new Line2D.Float(1350, 0,    0,    0)};
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

    public void setPaikka(Point2D.Float paikka) {
        this.paikka = paikka;
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
        setPaikka(kohde);
        return paikka;
    }

    /** 
     * @param kohde Piste, jota kohti käännytään.
     * @return Robotin uusi suunta asteina "vaaka-akselista" vastapäivään.
     */
    public float käännyKohti(Point2D.Float kohde) {
        float dx = kohde.x - paikka.x;
        float dy = kohde.y - paikka.y;
        suunta = (float)Math.toDegrees(Math.atan2(dy, dx));
        suunta += (suunta < 0 ? 360 : 0);
        return suunta;
    }

    /**
     * Lasketaan mittauskulmista nähtyjen lähimpien esteiden etäisyydet.
     * 
     * Tässä olis ideana, että verrataan robotin yhtä "näköviivaa" kaikkiin
     * kartan viivoihin vuoron perään ja jos leikkaus löytyy niin 
     * tallennetaan se pieninleikkaus-muuttujaan, jota vertaillaan tuleviin
     * leikkauspituuksiin. Sitten tallennetaan pieninleikkaus tauluun ja 
     * siirrytään tarkastelemaan seuraavaa näköviivaa. Lopuksi 
     * pieninleikkaus-muuttujista muodostettu taulu palautetaan kusujalle.
     * 
     * @param mittausMaara Mitattavien kulmien lukumäärä
     * @return IR-sensorin palauttamat etäisyydet lähimpiin esteisiin.
     */
    public float[] mittaa(int mittausMaara) {
        float taulu[] = new float[mittausMaara];
        double pieninetaisyys;
        nakyma = new JsimRoboNakyma(paikka, suunta, mittausMaara, infraKantama);

        for (int i = 0; i < nakyma.getNakotaulu().length; i++) {
            pieninetaisyys = infraKantama;
            for (int k = 0; k < kartta.length; k++) {
                Point2D.Float leikkauspiste = nakyma.laskeLeikkauspiste(
                    nakyma.getNakotaulu()[i], kartta[k]);
                
                if (leikkauspiste == null)
                    continue;

                double etaisyys = paikka.distance(leikkauspiste);
                if (etaisyys < pieninetaisyys)
                    pieninetaisyys = etaisyys;
            }
            taulu[i] = (float)pieninetaisyys;
        }

        return taulu;
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
        double x = paikka.x + matka * Math.cos(a);
        double y = paikka.y + matka * Math.sin(a);

        paikka = new Point2D.Float((float)x, (float)y);
        return paikka;
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

    // TÄÄ ON IHAN OUT-OF-DATE, PLIIS JUHO PORTTAATKO TÄN RoboOhjaimeen.
    private void valitseUusiPiste(int mittausMaara) {
        JsimData mittaus = new JsimData(suunta, mittaa(mittausMaara), paikka);
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
        } else if (tyhjyysmuisti == 0) { //kaikki havaitaittu kaikkialla
            if (edettytäyteen) {     //BOOLEAN EDETTYTÄYTEEN on siirretty tämän metodin ulkopuolelle
                edettytäyteen = false;
                käänny(180);
            } else {
                etene(mtaulu[19] / 2);    // edetään suoraan eteenpäin puolet eteenpäin mitatusta pituudesta
                edettytäyteen = true;
            }
        } else {
            käänny(((tyhjyysalkumuisti * 5) - 90) + ((tyhjyysmuisti / 2) * 5)); //witness the true power of mathematics!!!
            if ((tyhjyysalkumuisti - 1) >= 0) {
                etene((mtaulu[tyhjyysalkumuisti - 1] + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
            } else {
                etene((0 + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
            }
        }
    }

private class JsimData {

    /*
     * Robotin mittauksista luotu data
     */
    private float robosuunta;
    private float data[];
    private Point2D.Float paikka;

    /**
     * @param parametri robotin suunta
     * @param parametri mittaus-taulukko
     */
    public JsimData(float suunta, float[] data, Point2D.Float paikka) {
        robosuunta = suunta;
        this.data = data;
        this.paikka = paikka;
    }

    /**
     * @return robotin suunnan tältä mittaukselta
     */
    public float getRobosuunta() {
        return robosuunta;
    }

    public Point2D.Float getPaikka() {
        return paikka;
    }

    /**
     * @return robotin luoman data-taulukon
     */
    public float[] getData() {
        return data;
    }

    /**
     * @return robotin luoman data-taulukon integgerinä
     */
    public int[] getIntData() {
        System.out.println("***getintdata***");
        int[] dtaulu = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            dtaulu[i] = (int) data[i];
        }
        return dtaulu;
    }

    /**
     * Palauttaa datan pisteinä. Jos pistettä ei ole,
     * palauttaa pisteen (666666,666666)
     * 
     * @return data Point2D.Float olioina
     */
    public Point2D.Float[] getPisteet() {

        Point2D.Float[] datapisteet = new Point2D.Float[data.length];

        for (int i = 0; i < data.length; i++) {
            float kulma = (i * 180) / (data.length - 1);
            if (data[i] != 9999) {
                if ((robosuunta + kulma - 90) == -270) {   //W //länsi
                    float x = paikka.x - data[i];
                    float y = paikka.y;
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) == 0) {   //N //pohjoinen
                    float x = paikka.x;
                    float y = paikka.y + data[i];
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) < 90) {   //NE //koilinen
                    float x = (float) (paikka.x + data[i] * Math.sin((robosuunta + kulma - 90) * (Math.PI / 180)));
                    float y = (float) (paikka.y + data[i] * Math.cos((robosuunta + kulma - 90) * (Math.PI / 180)));
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) == 90) {  //E //itä
                    float x = paikka.x + data[i];
                    float y = paikka.y;
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) < 180) { //SE //kaakko
                    float x = (float) (paikka.x + data[i] * Math.sin((180 - (robosuunta + kulma - 90)) * (Math.PI / 180)));
                    float y = (float) (paikka.y - data[i] * Math.cos((180 - (robosuunta + kulma - 90)) * (Math.PI / 180)));
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) == 180) { //S //etelä
                    float x = paikka.x;
                    float y = paikka.y - data[i];
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) < 270) {  //SW //lounas omnomnom
                    float x = (float) (paikka.x - data[i] * Math.sin(((-180 + robosuunta + kulma - 90)) * (Math.PI / 180)));
                    float y = (float) (paikka.y - data[i] * Math.cos(((-180 + robosuunta + kulma - 90)) * (Math.PI / 180)));
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) == 270) { //W //länsi
                    float x = paikka.x - data[i];
                    float y = paikka.y;
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) < 360) {  //NW //luode
                    float x = (float) (paikka.x - data[i] * Math.sin(((360 - robosuunta + kulma - 90)) * (Math.PI / 180)));
                    float y = (float) (paikka.y + data[i] * Math.cos(((360 - robosuunta + kulma - 90)) * (Math.PI / 180)));
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) == 360) { //N //pohjoinen
                    float x = paikka.x;
                    float y = paikka.y + data[i];
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) < 450) {  //NW //koilinen
                    float x = (float) (paikka.x + data[i] * Math.sin(((-360 + robosuunta + kulma - 90)) * (Math.PI / 180)));
                    float y = (float) (paikka.y + data[i] * Math.cos(((-360 + robosuunta + kulma - 90)) * (Math.PI / 180)));
                    datapisteet[i] = new Point2D.Float(x, y);
                } else if ((robosuunta + kulma - 90) == 450) { //E //itä
                    float x = paikka.x + data[i];
                    float y = paikka.y;
                    datapisteet[i] = new Point2D.Float(x, y);
                }
            } else {
                datapisteet[i] = new Point2D.Float(666666, 666666);//ei pistettä
            }

        }
        return datapisteet;
    }
}
}
