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
    }
    
    

    @Override
    public void paintComponent(Graphics g) {
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
                    g.setColor(Color.red);
                    g.drawLine(temp.x, temp.y, piste.x, piste.y);
                }
                temp = piste;
            }
            if(ensimmainen == null){
                JOptionPane.showMessageDialog(this, "Point-ensimmäinen is null");
            }
            g.drawLine(0, getHeight(), ensimmainen.x, ensimmainen.y);
            g.drawLine(0, getHeight(), viimeinen.x, viimeinen.y);
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
