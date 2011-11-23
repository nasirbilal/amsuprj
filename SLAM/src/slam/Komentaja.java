package slam;

import java.awt.geom.Point2D;
import javax.swing.UIManager;

/**
 *
 * @author Olli Koskinen
 */
public class Komentaja {

    private UIRoboNakyma roboNakyma1;
    private UIRoboNakyma roboNakyma2;
    private UIKarttaNakyma karttaNakyma;

    public Komentaja() {
    }

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
            p[i].y = -(Math.cos(Math.toRadians(angles[i])) * dist);
            p[i].x = (Math.sin(Math.toRadians(angles[i])) * dist);
        }
        roboNakyma1.piirraEtaisyydet(p);

        for (int i = 0; i < p2.length; i++) {
            p2[i] = new Point2D.Double();

            dist = 60 + r.nextInt(20);
            p2[i].y = -(Math.cos(Math.toRadians(angles[i])) * dist);
            p2[i].x = (Math.sin(Math.toRadians(angles[i])) * dist);
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        Komentaja komentaja = new Komentaja();
        UI ui = new UI(komentaja);
        komentaja.roboNakymaKoe();
    }
}
