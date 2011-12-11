/*
 * Luokka käyttöliittymälle.
 */
package slam;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private JFrame debugIkkuna;
    private JPanel debugIkkunaPaneeli;
    private Komentaja komentaja;
    private UIRoboNakyma robo1;
    private UIRoboNakyma robo2;
    private UIKarttaNakyma karttaNakyma;
    private JPanel paaPaneeli;
    private JPanel nappulaPaneeli;
    private JPanel roboPaneeli;
    private JTextArea debugTekstit;
    private Border reunus;
    private JScrollPane scrollPane;
    private JButton debugValitsinN, paivitaNakymaN;
    private GridBagConstraints gbc;
    private boolean debug;
    private PrintWriter tuloste;

    /**
     * Konstruktori
     * @param commander 
     */
    public UI(Komentaja commander) {
        super("SLAM");
        debug = true;
        karttaNakyma = null;
        rekisteroiKomentaja(commander);

        ToolTipManager.sharedInstance().setInitialDelay(5);
        alustaKomponentit();

        //Keskitetään ikkuna
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
        debugTekstit = new JTextArea();
        scrollPane = new JScrollPane(debugTekstit);

        //isoon N-kirjaimeen loppuvat muuttujat ovat nappuloita.
        debugValitsinN = new JButton("Debug OFF");
        paivitaNakymaN = new JButton("Päivitä Näkymä");

        //Asetukset 
        nappulaPaneeli.setPreferredSize(new Dimension(140, 20));
        debugValitsinN.setMinimumSize(new Dimension(140, 20));
        paivitaNakymaN.setMinimumSize(new Dimension(140, 20));

        robo1.setPreferredSize(new Dimension(200, 200));
        robo2.setPreferredSize(new Dimension(200, 200));
        debugTekstit.setLineWrap(true);
        debugTekstit.setEditable(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        try {
            //Debug-viestien tallennuspaikka
            tuloste = new PrintWriter("debuguloste.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }


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
        gbc.gridy = 2;
        nappulaPaneeli.add(debugValitsinN, gbc);
        gbc.gridy = 3;
        nappulaPaneeli.add(paivitaNakymaN, gbc);
        gbc.gridy = 4;
        gbc.gridy = 5;
        roboPaneeli.add(robo1, BorderLayout.NORTH);
        roboPaneeli.add(robo2, BorderLayout.SOUTH);
        //paaPaneeli.add(nappulaPaneeli, BorderLayout.EAST);
        paaPaneeli.add(roboPaneeli, BorderLayout.WEST);
        paaPaneeli.add(karttaNakyma, BorderLayout.CENTER);


        //DebugIkkunan säädöt
        debugIkkuna = new JFrame("Debugging...");
        debugIkkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        debugIkkuna.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height));
        debugIkkunaPaneeli = new JPanel();
        debugIkkunaPaneeli.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height));
        debugTekstit.setBackground(Color.BLACK);
        debugTekstit.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 10, Toolkit.getDefaultToolkit().getScreenSize().height - 10));
        debugTekstit.setRows(75);
        debugTekstit.setForeground(Color.green);
        debugIkkunaPaneeli.add(scrollPane);
        debugIkkuna.setContentPane(debugIkkunaPaneeli);
        debugIkkuna.setVisible(true);



        // paaPaneeli.add(scrollPane, BorderLayout.SOUTH);

        //Nappuloiden toiminnot

        /* debugValitsinN.addActionListener(new ActionListener() {
        
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
        });*/

        karttaNakyma.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Line2D.Float[] robo1 = komentaja.annaRobo1Koordinaatit();
                Line2D.Float[] robo2 = komentaja.annaRobo2Koordinaatit();
                Point2D pRobo1 = robo1[0].getP1();
                Point2D pRobo2 = robo2[0].getP1();

                //Lasketaan kumpi robo on lähempänä klikattua pistettä 
                //ja lähetämme sen pisteeseen
                int delta1X = (int) (pRobo1.getX() - x);
                int delta1Y = (int) (pRobo1.getY() - y);
                int delta2X = (int) (pRobo2.getX() - x);
                int delta2Y = (int) (pRobo2.getY() - y);

                int robo1Matka = ((delta1X * delta1X) + (delta1Y * delta1Y));
                int robo2Matka = ((delta2X * delta2X) + (delta2Y * delta2Y));

                //Ei tarvitse laskea erikseen neliöjuurta;
                //Isomman luvun omaava on kauempana
                //Kiitti Lauri <3
                if (robo1Matka < robo2Matka) {
                    komentaja.asetaRobo1Paikka(new Point2D.Float(x, y));
                    System.out.println("Käskettiin 1 Robotin ajamaan pisteeseen: " + x + "," + y);
                } else {
                    komentaja.asetaRobo2Paikka(new Point2D.Float(x, y));
                    System.out.println("Käskettiin 2 Robotin ajamaan pisteeseen: " + x + "," + y);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        paivitaNakymaN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                komentaja.paivitaNakymat();
                komentaja.roboNakymaKoe();
                java.util.Random r = new Random();
                //  asetaDebugTeksti("Este havaittu pisteessä: " + r.nextInt(80) + ", " + r.nextInt(80));
            }
        });

        //ESCillä close
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        getRootPane().getActionMap().put("Cancel", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
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
            debugTekstit.setCaretPosition(debugTekstit.getText().length());
            tuloste.println(str);
        }
    }
}
