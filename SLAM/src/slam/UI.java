/*
 * Luokka käyttöliittymälle.
 */
package slam;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
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
    private JPanel roboPaneeli;
    private Komentaja komentaja;
    private UIRoboNakyma robo1;
    private UIRoboNakyma robo2;
    private UIKarttaNakyma karttaNakyma;
    private JTextArea debugTekstit;
    private Border reunus;
    private JScrollPane scrollPane;
    private JButton suljeYhteysN, debugValitsinN,
            paivitaNakymaN, tallennaKarttaN, lopetaN;
    private boolean debug;
    private GridBagConstraints gbc;

    /**
     * Konstruktori
     * @param commander 
     */
    public UI(Komentaja commander) {
        super("SLAM");
        debug = true;
        karttaNakyma = null;
        rekisteroiKomentaja(commander);
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
        nappulaPaneeli.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        //TODO: poista testit kun nakyman saato on valmis
        robo1 = new UIRoboNakyma();
        robo2 = new UIRoboNakyma();
        komentaja.asetaRoboNakyma1(robo1);
        komentaja.asetaRoboNakyma2(robo2);
        karttaNakyma = new UIKarttaNakyma();
        komentaja.asetaKarttaNakyma(karttaNakyma);
        reunus = BorderFactory.createEtchedBorder();
        debugTekstit = new JTextArea(8, 50);
        scrollPane = new JScrollPane(debugTekstit);

        //isoon N-kirjaimeen loppuvat muuttujat ovat nappuloita.
        suljeYhteysN = new JButton("Sulje yhteys");
        debugValitsinN = new JButton("Debug OFF");
        paivitaNakymaN = new JButton("Päivitä Näkymä");
        tallennaKarttaN = new JButton("Tallenna Kartta");
        lopetaN = new JButton("Lopeta");

        //Asetukset 
        nappulaPaneeli.setPreferredSize(new Dimension(140,20));
        suljeYhteysN.setMinimumSize(new Dimension(140, 20));
        debugValitsinN.setMinimumSize(new Dimension(140, 20));
        paivitaNakymaN.setMinimumSize(new Dimension(140, 20));
        tallennaKarttaN.setMinimumSize(new Dimension(140, 20));
        lopetaN.setMinimumSize(new Dimension(140, 20));

        robo1.setPreferredSize(new Dimension(200, 200));
        robo2.setPreferredSize(new Dimension(200, 200));
        debugTekstit.setLineWrap(true);
        debugTekstit.setEditable(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        //reunukset
        debugTekstit.setBorder(reunus);
        nappulaPaneeli.setBorder(reunus);
        roboPaneeli.setBorder(reunus);

        //asetetaan paneelit
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 1;
        nappulaPaneeli.add(suljeYhteysN,gbc);
        gbc.gridy = 2;
        nappulaPaneeli.add(debugValitsinN,gbc);
        gbc.gridy = 3;
        nappulaPaneeli.add(paivitaNakymaN,gbc);
        gbc.gridy = 4;
        nappulaPaneeli.add(tallennaKarttaN,gbc);
        gbc.gridy = 5;
        nappulaPaneeli.add(lopetaN,gbc);
        roboPaneeli.add(robo1, BorderLayout.NORTH);
        roboPaneeli.add(robo2, BorderLayout.SOUTH);
        paaPaneeli.add(nappulaPaneeli, BorderLayout.EAST);
        paaPaneeli.add(roboPaneeli, BorderLayout.WEST);
        paaPaneeli.add(karttaNakyma, BorderLayout.CENTER);
        paaPaneeli.add(scrollPane, BorderLayout.SOUTH);

        //Nappuloiden toiminnot
        suljeYhteysN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        debugValitsinN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (debug) {
                    debug = false;
                    debugValitsinN.setText("Debug ON");
                } else {
                    debug = true;
                    debugValitsinN.setText("Debug OFF");
                }
            }
        });

        paivitaNakymaN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                komentaja.paivitaNakymat();
                komentaja.TESTIroboNakymaTESTI();
                java.util.Random r = new Random();
                asetaDebugTeksti("Este havaittu pisteessä: " + r.nextInt(80) + ", " + r.nextInt(80));
            }
        });

        tallennaKarttaN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        lopetaN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.gc();
                System.exit(0);
            }
        });

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
    public void asetaDebugTeksti(String str) {
        if (str != null && debug) {
            debugTekstit.append(str.concat("\n"));
        }
    }
}
