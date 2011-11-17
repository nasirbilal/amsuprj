package slam;

import java.io.*;
import lejos.pc.comm.*;

/**
 *
 * @author Mudi
 */
public class NXTBTYhteys extends Thread implements BTYhteys {

    private volatile boolean jatkuu;
    private JsimRobo robo;
    private BTPaketti paketti;
    private NXTConnector yhteys;
    private DataOutputStream dataUlos;
    private DataInputStream dataSisaan;
    private int odotusAikaMS;  //default odotusaika on 5 sek
    private boolean lahetys;
    
    public NXTBTYhteys(JsimRobo robo) {
        this.jatkuu = true;
        this.robo = robo;
        this.paketti = null;
        this.odotusAikaMS = 5;
        alustaYhteys();

    }

    private void alustaYhteys() {
        this.yhteys = new NXTConnector();

        try {
            // Luodaan yhteys robottiin nimen perusteella
            if (yhteys.connectTo(robo.getNimi())) {
                System.err.println("Yhdistaminen epaonnistui");
                System.exit(-1);
            }
        } catch (Exception e) {}
         //Luodaan input/output streamit
        this.dataUlos = yhteys.getDataOut();
        this.dataSisaan = yhteys.getDataIn();
    }

    @Override
    public void run() {
        if (robo != null) {
            while (jatkuu) {

            }
        }
    }
    
    
    @Override
    public BTPaketti lahetaJaVastaanota(BTPaketti paketti, int odotusAikaMs) {
        this.paketti = paketti;
        this.odotusAikaMS = odotusAikaMs;
        return paketti; 
    }

    @Override
    public void uudelleenKaynnista() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
