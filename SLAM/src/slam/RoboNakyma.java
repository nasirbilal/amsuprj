/**
 * Luokka jolla toteutetaan eri roboottien näkymät käyttöliittymässä.
 */
package slam;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Olli Koskinen
 */
public class RoboNakyma extends JPanel {

    private Point[] pisteet;

    /**
     * 
     */
    public RoboNakyma() {
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
        //Reunukset
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        //Piirretään viiva pisteiden välillä ja lopuksi vielä viivat
        // 'origosta' ensimmäiseen ja viimeiseen pisteeseen
        // tämä siis simuloi aluetta, jonka robotti näkee
        if (pisteet != null) {
            Point temp = null, ensimmainen = null, viimeinen = null;
            for (Point piste : pisteet) {
                if (ensimmainen == null) {
                    ensimmainen = piste;
                }
                viimeinen = piste;

                if (temp != null) {
                    g2.drawLine(temp.x, temp.y, piste.x, piste.y);
                }
                temp = piste;
            }
            if (ensimmainen == null) {
                JOptionPane.showMessageDialog(this, "Point-ensimmäinen is null");
            }
            g2.setStroke(katkoV);
            g2.setColor(Color.red);
            g2.drawLine((getWidth()/2), getHeight(), ensimmainen.x, ensimmainen.y);
            g2.drawLine((getWidth()/2), getHeight(), viimeinen.x, viimeinen.y);
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.black);
        }
    }

    /**
     * 
     * @param pisteet
     */
    public void piirraEtaisyydet(Point[] pisteet) {
        this.pisteet = pisteet;
        repaint();
    }
}
