/*
 * Luokka käyttöliittymälle.
 */


package slam;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Olli Koskinen
 */

public class UI extends JFrame {
    
    /**
     * Luokkamuuttujat
     */
    private JPanel paaPaneeli;
    private RoboNakyma Robo1;
    private RoboNakyma Robo2;
    private Komentaja komentaja;
    
    
    /**
     * Konstruktori
     */
    public UI(Komentaja commander) {
        super("SLAM");
        rekisteroiKomentaja(komentaja);
        alustaKomponentit();
    }

    /**
     * Alustetaan kaikki näkyvät komponentit
     */
    private void alustaKomponentit() {
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Keskitetään ikkuna keskelle ruutua
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.width) / 2));

        
        //Alustetaan muuttujat
        paaPaneeli = new JPanel();
        Robo1 = new RoboNakyma(komentaja);
        Robo2 = new RoboNakyma(komentaja);
        
        pack();
        setContentPane(paaPaneeli);
        setVisible(true);
    }
    
    /**
     * 
     * @param komentaja 
     * 
     */
    private void rekisteroiKomentaja(Komentaja komentaja){
        this.komentaja = komentaja;
    }
}
