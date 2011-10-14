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
public class UI extends JFrame{

    /**
     * Luokkamuuttujat
     */
    private JPanel paaPaneeli;
    private JPanel nappulaPaneeli;
    private JPanel roboPaneeli;
    private Komentaja komentaja;
    private RoboNakyma robo1;
    private RoboNakyma robo2;
    private KarttaNakyma karttaNakyma;
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

        //Keskitetään ikkuna keskelle ruutua
        setLocationRelativeTo(null);
    }

    /**
     * Alustetaan kaikki näkyvät komponentit
     */
    private void alustaKomponentit() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        //Alustetaan muuttujat
        paaPaneeli = new JPanel(new BorderLayout());
        roboPaneeli = new JPanel(new BorderLayout());
        nappulaPaneeli = new JPanel();
        nappulaPaneeli.setLayout(new BoxLayout(nappulaPaneeli, BoxLayout.Y_AXIS));
        robo1 = new RoboNakyma(komentaja);
        robo2 = new RoboNakyma(komentaja);
        karttaNakyma = new KarttaNakyma(komentaja);
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
        robo1.setPreferredSize(new Dimension(150, 150));
        robo2.setPreferredSize(new Dimension(150, 150));
        debugTekstit.setLineWrap(true);
        debugTekstit.setEditable(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        //reunukset
        debugTekstit.setBorder(reunus);
        nappulaPaneeli.setBorder(reunus);
        roboPaneeli.setBorder(reunus);
        roboPaneeli.setBorder(reunus);
      
        //asetetaan paneelit
        nappulaPaneeli.add(yhdistaN);
        nappulaPaneeli.add(suljeYhteysN);
        nappulaPaneeli.add(debugValitsinN);
        nappulaPaneeli.add(paivitaNakymaN);
        nappulaPaneeli.add(tallennaKarttaN);
        nappulaPaneeli.add(lopetaN);
        roboPaneeli.add(robo1,BorderLayout.NORTH);
        roboPaneeli.add(robo2,BorderLayout.SOUTH);
        paaPaneeli.add(nappulaPaneeli, BorderLayout.EAST);
        paaPaneeli.add(roboPaneeli, BorderLayout.WEST);
        paaPaneeli.add(karttaNakyma,BorderLayout.CENTER);
        paaPaneeli.add(scrollPane, BorderLayout.SOUTH);


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
