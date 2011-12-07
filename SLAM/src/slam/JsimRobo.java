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
    private Rectangle      reunat =  new Rectangle(0, 0, 1350, 2000);                        
    private Line2D.Float[] kartta = {new Line2D.Float(440,   0, 440, 750),
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
    private static int id_count = 0;      /// Robotin yksilötunnuksen juokseva numero.

    /**
     * 
     */
    public JsimRobo() {
        this(0, 800);
    }

    /**
     * 
     * @param suunta
     * @param infraKantama
     */
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

    /**
     * 
     * @return
     */
    public int getID() {
        return id;
    }

    /**
     * 
     * @return
     */
    public String getNimi() {
        String nimi;
        nimi =  getID() == 0 ? "Jantunen": "JsimRobo[" + getID() + "]";
        nimi =  getID() == 1 ? "Hiiri": "JsimRobo[" + getID() + "]";
        return nimi;
    }

    /**
     * 
     * @return
     */
    public Point2D.Float getPaikka() {
        return new Point2D.Float(paikka.x, paikka.y);
    }

    public void setKartta(Line2D.Float[] kartta) {
        this.kartta = kartta;
        
        reunat.x = reunat.y = Integer.MAX_VALUE;
        reunat.width = reunat.height = Integer.MIN_VALUE;
        for (Line2D.Float l : kartta) {
            if (l.x1 < reunat.x) reunat.x = (int) l.x1;
            if (l.x2 < reunat.x) reunat.x = (int) l.x2;
            if (l.x1 > reunat.width) reunat.width = (int) l.x1;
            if (l.x2 > reunat.width) reunat.width = (int) l.x2;
            if (l.y1 < reunat.y) reunat.y = (int) l.y1;
            if (l.y2 < reunat.y) reunat.y = (int) l.y2;
            if (l.y1 > reunat.height) reunat.height = (int) l.y1;
            if (l.y2 > reunat.height) reunat.height = (int) l.y2;
        }
        
        // Siirrä kehikkoa sen verran, että sen alkunurkka on origossa.
        reunat.width -= reunat.x;
        reunat.x -= reunat.x;
        reunat.height -= reunat.y;
        reunat.y -= reunat.y;
    }
    
    /**
     * 
     * @param paikka
     */
    public void setPaikka(Point2D.Float paikka) {
        if (paikka != null)
            this.paikka = new Point2D.Float(paikka.x, paikka.y);
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
        return getPaikka();
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
        //System.out.println(getNimi() + " @ [" + paikka.x + ", " + paikka.y +
         //                 ", suunta " + suunta + " deg.");
        
        float taulu[] = new float[mittausMaara];
        nakyma = new JsimRoboNakyma(paikka, suunta, mittausMaara, infraKantama);

        for (int i = 0; i < nakyma.getNakotaulu().length; i++) {
            double pieninetaisyys = infraKantama;
            for (int k = 0; k < kartta.length; k++) {
                Point2D.Float leikkauspiste = nakyma.laskeLeikkauspiste(
                    nakyma.getNakotaulu()[i], kartta[k]);
                
                if (leikkauspiste == null)
                    continue;

                double etaisyys = paikka.distance(leikkauspiste);
                if (etaisyys < pieninetaisyys){
                    pieninetaisyys = etaisyys;
                }
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
        return getPaikka();
    }

    /** 
     * @param aste Robotin kääntymä määrä väliltä -90 (oikealle) ja 90 (vasemmalle).
     * @return Robotin uusi suunta.
     */
    private float käänny(float aste) {
        suunta = (suunta + aste) % 360; /* Pakota suunta välille 0-359. */
        if (suunta < 0)
            suunta = suunta + 360;
        return suunta;
    }
}
