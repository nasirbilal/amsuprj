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

    public JsimBTYhteys(JsimRobo robo) {
        this.jatkuu = true;
        this.robo = robo;
        this.paketti = null;
    }

    @Override
    public void run() {
        ObjectOutputStream objUlos = null;
        ObjectInputStream objSisaan = null;
        if (robo != null){
            while (jatkuu) {


                try {
                    NXTConnector conn = new NXTConnector();


                    // Luodaan yhteys robottiin nimen perusteella
                    boolean yhteys = conn.connectTo(robo.getNimi());
                    if (!yhteys) {
                        System.err.println("YhdataSisaantaminen epaonnistui");
                        System.exit(-1);
                    }

                    //Luodaan input/output streamit
                    DataOutputStream dataUlos = conn.getDataOut();
                    DataInputStream dataSisaan = conn.getDataIn();
                    objUlos = new ObjectOutputStream(dataUlos);
                    objSisaan = new ObjectInputStream(dataSisaan);


                    long startingTime = System.nanoTime();
                    //Kirjoitetaan dataa Streamiin
                    try {
                        objUlos.writeObject(conn);
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
                        conn.close();
                    } catch (IOException ioe) {
                        System.out.println("IOException sulkiessa yhteytta:");
                        System.out.println(ioe.getMessage());
                    }
                } catch (IOException ex) {
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
        public Mittaustulokset haeMittaustulokset
        
            () {
        throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean Laheta
        (BTPaketti paketti
        
            ) {
        throw new UnsupportedOperationException("Not supported yet.");
        }
    
}
