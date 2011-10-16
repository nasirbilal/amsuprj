/**
 * Command
 */
package slam;

import java.awt.Point;

/**
 *
 * @author Olli Koskinen
 */
public class Komentaja {

    private RoboNakyma roboNakyma1;
    private RoboNakyma roboNakyma2;
    private boolean annettuNakyma = false;
    private KarttaNakyma karttaNakyma;

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
        Point[] p = new Point[8];
        Point[] p2 = new Point[8];
        for(int i = 0; i<p.length;i++){
            p[i] = new Point(r.nextInt(80), r.nextInt(80));
        }
        roboNakyma1.piirraEtaisyydet(p);
        
        for(int i = 0; i<p2.length;i++){
            p2[i] = new Point(r.nextInt(80), r.nextInt(80));
        }
        roboNakyma2.piirraEtaisyydet(p2);
    }
    
    //TODO: poista testit kun nakyman saato on valmis
    /**
     * 
     * @param rb
     */
    public void asetaRoboNakyma1(RoboNakyma rb){
        this.roboNakyma1 = rb;
    }
    
    /**
     * 
     * @param rb
     */
    public void asetaRoboNakyma2(RoboNakyma rb){
        this.roboNakyma2 = rb;
    }
    
    /**
     * 
     * @param karttaNakyma
     */
    public void asetaKarttaNakyma(KarttaNakyma karttaNakyma){
        this.karttaNakyma = karttaNakyma;
    }

    void paivitaNakymat() {
        roboNakyma1.repaint();
        roboNakyma2.repaint();
        karttaNakyma.repaint();
    }
}
