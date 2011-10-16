/**
 * KarttaNakyma -luokka on tarkoitettu koostetun kartan piirtämistä varten
 */
package slam;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author Olli Koskinen
 */
public class KarttaNakyma extends JPanel {

    private Random r = new Random();

    @Override
    public void paintComponent(Graphics g) {

        //2D -kuvioiden piirtämistä varten, duh
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(4));
        super.paintComponent(g);

        //TODO: poista testit kun nakyman saato on valmis
        //TestiNeliö
        g2.drawLine(30, getHeight() - 30, 400, getHeight() - 30);
        g2.drawLine(400, getHeight() - 30, 400, getHeight() - 400);
        g2.drawLine(400, getHeight() - 400, 30, getHeight() - 400);
        g2.drawLine(30, getHeight() - 400, 30, getHeight() - 30);

        if (r.nextFloat() > 0.6f) {
            g2.drawRect((r.nextInt(300) + 30), (r.nextInt(300) + 30), 50, 50);
        }
        g.setColor(Color.black);
    }
}
