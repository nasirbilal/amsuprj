package slam;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

/**
 *
 * @author Mudi
 */
public class BTYhteys extends Thread {
    private volatile boolean jatkuu;

    public BTYhteys() {
        this.jatkuu = true;
    }

    @Override
    public void run() {
            ObjectOutputStream objUlos = null;
            ObjectInputStream objSisaan = null;
        while (jatkuu) {


            try {
                NXTConnector conn = new NXTConnector();


                // Luodaan yhteys Jantuseen
                boolean yhteys = conn.connectTo("Jantunen");
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