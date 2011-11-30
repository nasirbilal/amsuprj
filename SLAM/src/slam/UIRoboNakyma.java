/**
 * Luokka jolla toteutetaan eri roboottien näkymät käyttöliittymässä.
 */
package slam;

import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.*;

/**
 *
 * @author Olli Koskinen
 */
public class UIRoboNakyma extends JPanel {

    private Point2D.Double[] pisteet;

    public UIRoboNakyma() {
        this.pisteet = null;
        final float katko[] = {10.0f};
        final BasicStroke katkoV = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, katko, 0.0f);
    }

    @Override
    public void paintComponent(Graphics g) {

        final float katko[] = {10.0f};
        final BasicStroke katkoV = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, katko, 0.0f);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        super.paintComponent(g);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        if (pisteet == null)
            return;

        // Laske sovituskertoimet saadulle datalle niin, että se mahtuu
        // ikkunaan. Tässä pitäisi data skaalata jotenkin muutein kuin koko
        // ikkunan kokoiseksi, esim. maksimietäisyyden mukaan, mutta tämä
        // hoitakoon homman toistaiseksi.
        double xmin = Float.MAX_VALUE;
        double xmax = Float.MIN_VALUE;
        double ymin = Float.MAX_VALUE;
        double ymax = Float.MIN_VALUE;
        for (Point2D.Double piste : pisteet)
            if (piste != null) {
            xmin = xmin > piste.x ? piste.x : xmin;
            xmax = xmax < piste.x ? piste.x : xmax;
            ymin = ymin > piste.y ? piste.y : ymin;
            ymax = ymax < piste.y ? piste.y : ymax;
        }
        double xratio = getWidth()/(xmax - xmin);
        double yratio = getHeight()/(ymax - ymin);

        //Piirretään viiva pisteiden välillä ja lopuksi vielä viivat
        // 'origosta' ensimmäiseen ja viimeiseen pisteeseen
        // tämä siis simuloi aluetta, jonka robotti näkee
        Point2D.Double edellinen = null, ensimmainen = null, viimeinen = null;
        for (Point2D.Double piste : pisteet) {
            if (piste == null)
                continue;

            piste.x = 5 + (piste.x - xmin) * xratio * 0.9;
            piste.y = getHeight() - (piste.y - ymin) * yratio * 0.9 - 5;

            if (ensimmainen == null) {
                ensimmainen = piste;
            }

            if (edellinen != null) {
                g2.drawLine((int) edellinen.x, (int) edellinen.y,
                        (int) piste.x, (int) piste.y);
            }

            viimeinen = edellinen = piste;
        }

        g2.setStroke(katkoV);
        g2.setColor(Color.red);

        //Pitää olla int,int,int,int ; ei int,int,double,double, tämä vain temp korjaus
        if (ensimmainen != null)
            g2.drawLine(getWidth() / 2, getHeight(),
                     (int) ensimmainen.x, (int) ensimmainen.y);
        if (viimeinen != null)
            g2.drawLine(getWidth() / 2, getHeight(),
                     (int) viimeinen.x, (int) viimeinen.y);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.black);
    }

    /**
     * 
     * @param pisteet
     */
    public void piirraEtaisyydet(Point2D.Double[] pisteet) {
        this.pisteet = pisteet;
        repaint();
    }
}
