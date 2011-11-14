package slam;

import java.io.*;
import java.util.logging.*;
import lejos.pc.comm.*;

/**
 *
 * @author Mudi
 */
public class JsimBTYhteys extends Thread implements BTYhteys {

    private volatile boolean jatkuu;
    private JsimRobo robo;
    private BTPaketti paketti;
    private NXTConnector yhteys;
    private DataOutputStream dataUlos;
    private DataInputStream dataSisaan;

    public JsimBTYhteys(JsimRobo robo) {
        this.jatkuu = true;
        this.robo = robo;
        this.paketti = null;
        this.yhteys = new NXTConnector();
        
        //Luodaan input/output streamit
        this.dataUlos = yhteys.getDataOut();
        this.dataSisaan = yhteys.getDataIn();
    }

    @Override
    public void run() {
        ObjectOutputStream objUlos = null;
        ObjectInputStream objSisaan = null;
        if (robo != null) {
            while (jatkuu) {

                try {
                    // Luodaan yhteys robottiin nimen perusteella
                    if (yhteys.connectTo(robo.getNimi())) {
                        System.err.println("Yhdistaminen epaonnistui");
                        System.exit(-1);
                    }

                    long startingTime = System.nanoTime();
                    //Kirjoitetaan dataa Streamiin
                    try {
                        objUlos.writeObject(yhteys);
                        objUlos.flush();
                    } catch (IOException ioe) {
                        System.out.println("IO Exception kirjoittaessa:");
                        System.out.println(ioe.getMessage());
                    }
                    //luetaan dataa
                    try {
                        objSisaan.readInt();
                    } catch (IOException ioe) {
                        System.out.println("IO Exception lukiessa:");
                        System.out.println(ioe.getMessage());
                    }
                    long endingTime = System.nanoTime();
                    System.out.println("Lähetettiin 100000 lukua ajassa " + (endingTime - startingTime) + " millisekunttia");
                    try {
                        dataSisaan.close();
                        dataUlos.close();
                        yhteys.close();
                    } catch (IOException ioe) {
                        System.out.println("IOException sulkiessa yhteytta:");
                        System.out.println(ioe.getMessage());
                    }
                } catch (Exception ex) {
                    Logger.getLogger(BTYhteys.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        objUlos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(BTYhteys.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    @Override
    public BTPaketti lahetaJaVastaanota(BTPaketti paketti, int odotusAikaMs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void uudelleenKaynnista() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
