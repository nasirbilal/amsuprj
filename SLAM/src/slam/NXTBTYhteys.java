package slam;

import java.awt.geom.Point2D;
import java.io.*;
import lejos.pc.comm.*;

/**
 *
 * @author Olli Koskinen
 */
public class NXTBTYhteys extends Thread implements BTYhteys {

    private volatile boolean jatkuu;
    private final int lukuMaara;
    private int tempEtaisyydet[];
    private JsimRobo robo;
    private BTPaketti paketti;
    private NXTConnector yhteys;
    private DataOutputStream dataUlos;
    private DataInputStream dataSisaan;
    private int odotusAikaMS;  //default odotusaika on 5 sek
    private volatile boolean lahetys;

    public NXTBTYhteys(JsimRobo robo) {
        this.jatkuu = true;
        this.robo = robo;
        this.paketti = null;
        this.tempEtaisyydet = new int[BTPaketti.MAARA];
        this.odotusAikaMS = 5;
        this.lahetys = false;
        this.lukuMaara = BTPaketti.MAARA + 1;  //Etaisyyksien maara + id ja itse maara lukema
        alustaYhteys();

    }

    private void alustaYhteys() {
        this.yhteys = new NXTConnector();
        try {
            // Luodaan yhteys robottiin nimen perusteella
            if(!yhteys.connectTo(robo.getNimi())) {
                System.err.println("Yhdistaminen epaonnistui");
                //uudelleenKaynnista();
            }
        } catch (Exception e) {
        }
        //Luodaan input/output streamit
        this.dataUlos = yhteys.getDataOut();
        this.dataSisaan = yhteys.getDataIn();
    }

    //Itse lähetys ja vastaanotto tapahtuu täällä
    /*vastaanottojärjestys:
     *1. ID
     *2. itse etäisyydet
     *3. nykysijainti
     *4. uusisijainti
     *5. mittausSuunta
     */
    @Override
    public void run() {
        if (robo != null) {
            while (jatkuu) {
                /*Jos kutsutaan lahetaJaVastaanota() -metodia, alamme toimimaan*/
                if (lahetys) {
                    try {
                        dataUlos.writeInt(paketti.getId());
                        dataUlos.writeFloat(paketti.getNykySijaiti().x);
                        dataUlos.writeFloat(paketti.getNykySijaiti().y);
                        dataUlos.writeFloat(paketti.getUusiSijaiti().x);
                        dataUlos.writeFloat(paketti.getUusiSijaiti().y);
                        dataUlos.writeFloat(paketti.getMittausSuunta().y);
                        dataUlos.writeFloat(paketti.getMittausSuunta().y);
                        dataUlos.flush();
                    } catch (Exception e) {
                        uudelleenKaynnista();
                    }
                    try {
                        for (int i = 0; i < lukuMaara; i++) {
                            if (i < 1) {
                                paketti.setId(dataSisaan.readInt());
                            } else {
                                tempEtaisyydet[i - 1] = dataSisaan.readInt();
                            }
                        }

                        paketti.setNykySijaiti(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));
                        paketti.setUusiSijaiti(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));
                        paketti.setMittausSuunta(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));

                    } catch (Exception e){
                        uudelleenKaynnista();
                    }
                    lahetys = false;
                }
            }
        }
    }
    
    //lahetaJaVastaanota vastaanottaa paketin josta lähetetään tieto robotille ja 
    //johon kirjoitetaan uudet tiedot
    
    @Override
    public BTPaketti lahetaJaVastaanota(BTPaketti paketti, int odotusAikaMs) {
        this.paketti = paketti;
        this.odotusAikaMS = odotusAikaMs;
        this.lahetys = true;
        
        //odotetaan kunnes paketti on päivitetty
        while (lahetys) {
        }
        return paketti;
    }

    @Override
    public void uudelleenKaynnista() {
        alustaYhteys();
    }
}
