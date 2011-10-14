/**
 * KarttaNakyma -luokka on tarkoitettu koostetun kartan piirtämistä varten
 */
package slam;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Olli Koskinen
 */
public class KarttaNakyma extends JPanel {
    private final Komentaja komentaja;

    KarttaNakyma(Komentaja komentaja) {
        this.komentaja = komentaja;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.gray);
        g.drawString("TestiTeksti", 20, 20);
    }
}
