/**
 * Luokka jolla toteutetaan eri roboottien näkymät käyttöliittymässä.
 */
package slam;

import java.awt.*;
import javax.swing.JPanel;

/**
 *
 * @author Olli Koskinen
 */
public class RoboNakyma extends JPanel {

    RoboNakyma(Komentaja komentaja) {
    }

    @Override
    public void paintComponent(Graphics g) {

        //Mahdollisia 2d kuvioita varten
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(5));

        super.paintComponent(g);
        g.setColor(Color.red);
        g2.fill3DRect(50, 50, 15, 15, true);
        g.drawString("TestiTeksti", 20, 20);
    }
}
