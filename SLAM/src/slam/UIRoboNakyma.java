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

        //Piirretään viiva pisteiden välillä ja lopuksi vielä viivat
        // 'origosta' ensimmäiseen ja viimeiseen pisteeseen
        // tämä siis simuloi aluetta, jonka robotti näkee
        if (pisteet != null) {
            Point2D.Double edellinen = null, ensimmainen = null, viimeinen = null;
            for (Point2D.Double piste : pisteet) {
                if (ensimmainen == null) {
                    ensimmainen = piste;
                }

                if (edellinen != null) {
                    g2.drawLine((int)edellinen.x+getWidth()/2,
                                (int)edellinen.y+getHeight(),
                                (int)piste.x+getWidth()/2,
                                (int)piste.y+getHeight());
                }

                viimeinen = edellinen = piste;
            }

            g2.setStroke(katkoV);
            g2.setColor(Color.red);

            //Pitää olla int,int,int,int ; ei int,int,double,double, tämä vain temp korjaus
            g2.drawLine(getWidth()/2, getHeight(),
                       (int)ensimmainen.x+getWidth()/2,
                       (int)ensimmainen.y+getHeight());
            g2.drawLine(getWidth()/2, getHeight(),
                       (int)viimeinen.x+getWidth()/2,
                       (int)viimeinen.y+getHeight());
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.black);
        }
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
