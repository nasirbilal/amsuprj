/**
 * UIKarttaNakyma -luokka on tarkoitettu koostetun kartan piirtämistä varten
 */
package slam;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author Olli Koskinen
 */
public class UIKarttaNakyma extends JPanel implements MouseMotionListener{

    private Line2D.Float[] janat;
    private Random r = new Random();



    @Override
    public void paintComponent(Graphics g) {

        if (janat == null)
            return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(4));
        super.paintComponent(g);

        // Laske sovituskertoimet saadulle datalle niin, että se mahtuu
        // ikkunaan. Tässä pitäisi data skaalata jotenkin muutein kuin koko
        // ikkunan kokoiseksi, esim. maksimietäisyyden mukaan, mutta tämä
        // hoitakoon homman toistaiseksi.
        
        float xmin = Float.MAX_VALUE;
        float xmax = Float.MIN_VALUE;
        float ymin = Float.MAX_VALUE;
        float ymax = Float.MIN_VALUE;
        for (Line2D.Float l : janat)
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
        double xratio = getWidth()/(xmax - xmin);
        double yratio = getHeight()/(ymax - ymin);

        for (Line2D.Float l : janat)
            if (l != null)
                g2.drawLine((int)((l.x1 - xmin) * xratio),
                        -(int)((l.y1 - ymin) * yratio) + getHeight(),
                         (int)((l.x2 - xmin) * xratio),
                        -(int)((l.y2 - ymin) * yratio) + getHeight());

        g.setColor(Color.black);
    }

    public UIKarttaNakyma() {
        addMouseMotionListener(this);
    }

    void piirraKartta(Line2D.Float[] janat) {
        this.janat = janat;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       setToolTipText(e.getX()+","+e.getY());
    }
}
