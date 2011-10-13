/*
 * Luokka käyttöliittymälle.
 */


package slam;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

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
    private JTextArea debugTekstit;
    private Border reunus;
    
    
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
        setSize(800,600);
        //Keskitetään ikkuna keskelle ruutua
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.width) / 2));

        
        //Alustetaan muuttujat
        paaPaneeli = new JPanel(new GridBagLayout());
        Robo1 = new RoboNakyma(komentaja);
        Robo2 = new RoboNakyma(komentaja);
        reunus = BorderFactory.createEtchedBorder();
        debugTekstit = new JTextArea(5,60);
        
        //reunukset
        debugTekstit.setBorder(reunus);
        
        //asetetaan paneelit
        paaPaneeli.add(debugTekstit);
        
        
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
