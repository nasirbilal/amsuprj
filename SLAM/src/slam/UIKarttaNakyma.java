/**
 * UIKarttaNakyma -luokka on tarkoitettu koostetun kartan piirtämistä varten
 */
package slam;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author Olli Koskinen
 */
public class UIKarttaNakyma extends JPanel implements MouseMotionListener {

    private Line2D.Float[] janat;
    private Line2D.Float[][] robottiNakymat;
    private Random r = new Random();

    //TODO: doublebuffering
    @Override
    public void paintComponent(Graphics g) {

        if (janat == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(2));

        super.paintComponent(g);
        setDoubleBuffered(true);
        // Laske sovituskertoimet saadulle datalle niin, että se mahtuu
        // ikkunaan. Tässä pitäisi data skaalata jotenkin muutein kuin koko
        // ikkunan kokoiseksi, esim. maksimietäisyyden mukaan, mutta tämä
        // hoitakoon homman toistaiseksi.

        float xmin = Float.MAX_VALUE;
        float xmax = Float.MIN_VALUE;
        float ymin = Float.MAX_VALUE;
        float ymax = Float.MIN_VALUE;
        for (Line2D.Float l : janat) {
            if (l != null) {
                xmin = xmin > l.x1 ? l.x1 : xmin;
                xmax = xmax < l.x1 ? l.x1 : xmax;
                ymin = ymin > l.y1 ? l.y1 : ymin;
                ymax = ymax < l.y1 ? l.y1 : ymax;
                xmin = xmin > l.x2 ? l.x2 : xmin;
                xmax = xmax < l.x2 ? l.x2 : xmax;
                ymin = ymin > l.y2 ? l.y2 : ymin;
                ymax = ymax < l.y2 ? l.y2 : ymax;
            }
        }
        double xratio = getWidth() / (xmax - xmin);
        double yratio = getHeight() / (ymax - ymin);
        double ratio = Math.min(xratio, yratio);

        for (Line2D.Float l : janat) {
            if (l != null) {
                g2.drawLine((int) ((l.x1 - xmin) * ratio),
                        -(int) ((l.y1 - ymin) * ratio) + getHeight(),
                        (int) ((l.x2 - xmin) * ratio),
                        -(int) ((l.y2 - ymin) * ratio) + getHeight());
            }
        }

        if (robottiNakymat == null) {
            return;
        }

        for (int i = 0; i < robottiNakymat.length; ++i) {
            if (robottiNakymat[i] == null || robottiNakymat[i].length == 0)
                continue;

            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(1));

            // Piirrä robotin näköviivat.
            for (Line2D.Float l : robottiNakymat[i])
                g2.drawLine((int) ((l.x1 - xmin) * ratio),
                        (int) ((l.y1 - xmin) * ratio),
                        (int) ((l.x2 - xmin) * ratio),
                        (int) ((l.y2 - xmin) * ratio));                
            
            g2.setColor(i % 2 == 0 ? Color.GREEN : Color.BLUE);
            g2.setStroke(new BasicStroke(5));

            // Piirrä itse robotti.
            Point2D.Float p = (Point2D.Float)robottiNakymat[i][0].getP1();
            g2.drawLine((int) ((p.x - xmin) * ratio),
                    (int) ((p.y - xmin) * ratio),
                    (int) ((p.x - xmin) * ratio),
                    (int) ((p.y - xmin) * ratio));
        }
    }

    /**
     * 
     */
    public UIKarttaNakyma() {
        addMouseMotionListener(this);
    }

    void piirraKartta(Line2D.Float[] janat, Line2D.Float[][] robottiNakymat) {
        this.janat = janat;
        this.robottiNakymat = robottiNakymat;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setToolTipText(e.getX() + "," + e.getY());
    }
}
