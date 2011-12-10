package slam;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.UIManager;

/**
 *
 * @author Olli Koskinen
 */
public class Komentaja extends Thread {

    private UIRoboNakyma roboNakyma1;
    private UIRoboNakyma roboNakyma2;
    private UIKarttaNakyma karttaNakyma;
    private UI ui;
    
    BTYhteys b1;
    BTYhteys b2;
    
    private RoboOhjain r1;
    private RoboOhjain r2;
    
    /**
     * 
     */
    public void roboNakymaKoe() {
        // TODO: tee RoboOhjaimen yksikkötestit ja syötä tulos tänne samalla!

        java.util.Random r = new java.util.Random();
        Point2D.Double[] p = new Point2D.Double[9];
        Point2D.Double[] p2 = new Point2D.Double[9];
        final double[] angles = {-80, -60, -40, -20, 0, 20, 40, 60, 80};
        double dist;
        for (int i = 0; i < angles.length; i++) {
            p[i] = new Point2D.Double();
            dist = 60 + r.nextInt(20);
            p[i].x = (Math.cos(Math.toRadians(90+angles[i])) * dist);
            p[i].y = (Math.sin(Math.toRadians(90+angles[i])) * dist);
        }
        roboNakyma1.piirraEtaisyydet(p);

        for (int i = 0; i < p2.length; i++) {
            p2[i] = new Point2D.Double();

            dist = 60 + r.nextInt(20);
            p2[i].x = (Math.cos(Math.toRadians(90+angles[i])) * dist);
            p2[i].y = (Math.sin(Math.toRadians(90+angles[i])) * dist);
        }
        roboNakyma2.piirraEtaisyydet(p2);
    }

    /**
     * 
     * @param rb
     */
    public void asetaRoboNakyma1(UIRoboNakyma rb) {
        this.roboNakyma1 = rb;
    }

    /**
     * 
     * @param rb
     */
    public void asetaRoboNakyma2(UIRoboNakyma rb) {
        this.roboNakyma2 = rb;
    }

    /**
     * 
     * @param karttaNakyma
     */
    public void asetaKarttaNakyma(UIKarttaNakyma karttaNakyma) {
        this.karttaNakyma = karttaNakyma;
    }

    void paivitaNakymat() {
        roboNakyma1.repaint();
        roboNakyma2.repaint();
        karttaNakyma.repaint();
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        //Muutetaan ohjelman ulkonäkö windows tyyliseksi
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
       Komentaja komentaja = new Komentaja();
       UI  ui = new UI(komentaja);
       komentaja.rekisteroiUI(ui);
       komentaja.start();
    }

    @Override
    public void run() {
        if (true) {
            b1 = new NXTBTYhteys(this.ui);
           // b2 = new NXTBTYhteys(this.ui);
        }else{
            b1 = new JsimBTYhteys();
            b2 = new JsimBTYhteys();
        }
        b1.start();
       // b2.start();
        
        r1 = new RoboOhjain(b1, 10*1000, 80*10);
        //r2 = new RoboOhjain(b2, 10*1000, 80*10);

        // HUOM HUOM TÄSSÄ ON ONGELMA! Joskus piirto tapahtuu vanhasta
        // sijainnista mutta uusilla mittaustuloksilla == VIRHE!
        // Aikapulan takia on tehty tämä puukotus, jossa "säikeitä" kutsutaan
        // synkronisesti yksi kerrallaan eli odotetaan, että robotti N saa
        // mittaukset tehtyä ennen kuin siirrytään ajamaan robottia N+1.
//        Thread t1 = new Thread(r1);
//        Thread t2 = new Thread(r2);
          
//        t1.start();
//        t2.start();

        Line2D.Float[][] robottienNakymat = new Line2D.Float[2][];
        Kokoaja.asetaVirhemitat(Math.toRadians(5.0), 1.0*10);
        while (roboNakyma1 != null/* && roboNakyma2 != null*/) {
            boolean muuttunut = false;
            
            r1.suorita();
            if (r1.onMuuttunut()) {
                roboNakyma1.piirraEtaisyydet(r1.haeEtaisyydet());
                Kokoaja.asetaKartta(b1.getRoboID(), r1.haeKartta());
                robottienNakymat[0] = r1.annaKoordinaatit();
                muuttunut = true;
            }
            
           /* r2.suorita();
            if (r2.onMuuttunut()) {
                roboNakyma2.piirraEtaisyydet(r2.haeEtaisyydet());
                Kokoaja.asetaKartta(b2.getRoboID(), r2.haeKartta());
                robottienNakymat[1] = r2.annaKoordinaatit();
                muuttunut = true;
            }
*/
            if (muuttunut)
                karttaNakyma.piirraKartta(Kokoaja.yhdista(), robottienNakymat);
        }
    }
    
    //Käyttöliittymältä tuleva liikkumiskäsky, ohittaa robotin uuden pisteen laskun
    /**
     * 
     * @param p
     */
    public void asetaRobo1Paikka(Point2D.Float p){
        r1.liikuTahan(p);
    }
    
    /**
     * 
     * @param p
     */
    public void asetaRobo2Paikka(Point2D.Float p){
        r2.liikuTahan(p);
    }
    
    /**
     * 
     * @return
     */
    public Line2D.Float[] annaRobo1Koordinaatit(){
        return r1.annaKoordinaatit();
    }
    /**
     * 
     * @return
     */
    public Line2D.Float[] annaRobo2Koordinaatit(){
        return r2.annaKoordinaatit();
    }
    

    private void rekisteroiUI(UI ui) {
        this.ui = ui;
    }
}
