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
    private JPanel nappulaPaneeli;
    private RoboNakyma Robo1;
    private RoboNakyma Robo2;
    private Komentaja komentaja;
    private JTextArea debugTekstit;
    private Border reunus;
    private JScrollPane scrollPane;
    private JButton suljeYhteysN;
    private JButton yhdistaN;
    private JButton debugValitsinN;
    private JButton paivitaNakymaN;
    private JButton tallennaKarttaN;
    private JButton lopetaN;

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
        setSize(800, 600);
        //Keskitetään ikkuna keskelle ruutua
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.width) / 2));


        //Alustetaan muuttujat
        paaPaneeli = new JPanel(new BorderLayout());
        nappulaPaneeli = new JPanel();
        nappulaPaneeli.setLayout(new BoxLayout(nappulaPaneeli, BoxLayout.Y_AXIS));
        Robo1 = new RoboNakyma(komentaja);
        Robo2 = new RoboNakyma(komentaja);
        reunus = BorderFactory.createEtchedBorder();
        debugTekstit = new JTextArea(8, 50);
        scrollPane = new JScrollPane(debugTekstit);
     
        //isoon N-kirjaimeen loppuvat muuttujat ovat nappuloita.
        suljeYhteysN = new JButton("Sulje yhteys");
        yhdistaN = new JButton("Yhdistä");
        debugValitsinN = new JButton("Debug ON");
        paivitaNakymaN = new JButton("Päivitä Näkymä");
        tallennaKarttaN = new JButton("Tallenna Kartta");
        lopetaN = new JButton("Lopeta");
        
        //Asetukset 
        debugTekstit.setLineWrap(true);
        debugTekstit.setEditable(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //reunukset
        debugTekstit.setBorder(reunus);
        nappulaPaneeli.setBorder(reunus);

        //asetetaan paneelit
        paaPaneeli.add(nappulaPaneeli,BorderLayout.EAST);
        paaPaneeli.add(scrollPane,BorderLayout.SOUTH);
        nappulaPaneeli.add(yhdistaN);
        nappulaPaneeli.add(suljeYhteysN);
        nappulaPaneeli.add(debugValitsinN);
        nappulaPaneeli.add(paivitaNakymaN);
        nappulaPaneeli.add(tallennaKarttaN);
        nappulaPaneeli.add(lopetaN);
        
        setContentPane(paaPaneeli);
        setVisible(true);
    }

    /**
     * 
     * @param komentaja 
     * 
     */
    private void rekisteroiKomentaja(Komentaja komentaja) {
        this.komentaja = komentaja;
    }

    
    /**
     * Debug tekstin näyttäminen ruudulla, lisätään stringin loppuun rivinvaihto,
     * selkeyden vuoksi.
     * 
     * @param str 
     */
    protected void asetaDebugTeksti(String str) {
        if (str != null) {
            debugTekstit.append(str.concat("\n"));
        }
    }
}
