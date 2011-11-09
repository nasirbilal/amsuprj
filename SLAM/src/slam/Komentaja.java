package slam;

import java.awt.geom.Point2D;
import javax.swing.UIManager;

/**
 *
 * @author Olli Koskinen
 */
public class Komentaja {
    private final double SKAALA  = (double)200/80;
    private UIRoboNakyma roboNakyma1;
    private UIRoboNakyma roboNakyma2;
    private UIKarttaNakyma karttaNakyma;
    

    /**
     * 
     */
    public Komentaja() {
    
    }

    //TODO: poista testit kun nakyman saato on valmis
    /**
     * 
     */
    public void TESTIroboNakymaTESTI() {
        java.util.Random r = new java.util.Random();
        Point2D.Double[] p = new Point2D.Double[9];
        Point2D.Double[] p2 = new Point2D.Double[9];
        final double[] angles = {-90, -70, -50, -30, 0, 20, 50, 70, 90};
        double[] dist = new double[9];
        for (int i = 0; i < dist.length; i++) {
            p[i] = new Point2D.Double();
            dist[i] = r.nextInt(80);
            p[i].x = (Math.cos(angles[i]) * dist[i])*SKAALA;
            p[i].y = (Math.sin(angles[i]) * dist[i])*SKAALA;
        }
        roboNakyma1.piirraEtaisyydet(p);

        for (int i = 0; i < p2.length; i++) {
            p2[i] = new Point2D.Double();
            
            dist[i] = r.nextInt(80);
            p2[i].x = (Math.cos(angles[i]) * dist[i])*SKAALA;
            p2[i].y = (Math.sin(angles[i]) * dist[i])*SKAALA;
        }
        roboNakyma2.piirraEtaisyydet(p2);
    }

    //TODO: poista testit kun nakyman saato on valmis
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
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}
        Komentaja komentaja = new Komentaja();
        UI ui = new UI(komentaja);
    }

    void yhdista() {
        // luoda kartta eli kokoaja
        // luoda jsimrobot
        // arpoa robolle sijainti
        // jokaiselle robotille luodaan säie, joka kuuntelee robotilta tulevia
        // mittaustuloksia ja kun uudet tulokset tulevat, annetaan ne
        // ensin roboNakymalle, joka piirtää viivat robotin mittaamien pisteiden
        // välille: roboNakyma1.piirraEtaisyydet(p). Sen jälkeen mittaustulokset annetaan
        // Kokoajalle, joka sylkee uuden kartan, joka sitten piirretään ruudulle.
        // Lopuksi säie kutsuu paivitaNakymat().
        JsimRobo robo = new JsimRobo(); //robotti
        Mittaustulokset sailio = new Mittaustulokset(); //sailio mihin tallennetaan mittaustulokset
        Kerailija kerailija = new Kerailija(robo,sailio); //luodaan keräilijä säie
        kerailija.start();
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
        kerailija.lopeta();
        sailio.tulosta();
        
        
        // EXTRAAAAA JA BOOONUSTA:
        // robotti ei tiedä, missä se on. Tietokone laskee robotin todellisen sijainnin
        // mittausetäisyyksien perusteella. Säie luettuaan robotin mittaustulokset
        // laskee robotin sijainnin, uuden mittauspisteen ja käskee robotin mennä
        // uuteen pisteeseen mittaamaan. Sitten säie alkaa alusta.
    }
}
