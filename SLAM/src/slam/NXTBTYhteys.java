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
    private int tempEtaisyydet[];
    private JsimRobo robo;
    private BTPaketti paketti;
    private NXTConnector yhteys;
    private DataOutputStream dataUlos;
    private DataInputStream dataSisaan;
    //  private int odotusAikaMS;  //default odotusaika on 5 sek
    public static volatile boolean varattu = true;
    private int yrityksia;
    private final String nimi;
    private volatile boolean luku;
    private volatile boolean kirjoitus;
    private volatile boolean muuttunut;

    /**
     * 
     * @param robo
     */
    public NXTBTYhteys(String str) {
        this.yrityksia = 0;
        this.robo = new JsimRobo();
        this.paketti = new BTPaketti(robo.getID());                            //BTPaketti
        this.tempEtaisyydet = new int[BTPaketti.MAARA];
        // this.odotusAikaMS = 5;                          //"Connection timeout" -EI IMPLEMENTOITU
        this.yhteys = new NXTConnector();
        this.nimi = str;
        this.jatkuu = true;
        this.luku = false;
        this.kirjoitus = false;
        this.muuttunut = false;

        alustaYhteys();
    }

    @Override
    public int getRoboID() {
        return robo != null ? robo.getID() : -1;
    }

    private void alustaYhteys() {
        try {
            // Luodaan yhteys robottiin nimen perusteella
            if (!yhteys.connectTo(nimi)) {
                yrityksia++;
                System.err.println("Yhdistaminen epaonnistui");
                if (yrityksia < 5) {
                    uudelleenKaynnista();
                }
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Luodaan input/output streamit
        this.dataUlos = yhteys.getDataOut();
        this.dataSisaan = yhteys.getDataIn();
    }

    //Itse lähetys ja vastaanotto tapahtuu täällä
    /*Lukujärjestys
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
                if (kirjoitus) {
                    try {
                        System.out.println("");
                        System.out.println("#######KIRJOITUS ALKAA#######");
                        dataUlos.writeInt(paketti.getId());
                        dataUlos.flush();
                        Thread.sleep(50);
                        System.out.println("ID kirjoitettu");
                        dataUlos.writeFloat(paketti.getNykySijainti().x);
                        dataUlos.flush();
                        Thread.sleep(50);
                        System.out.println("Nykysijainti X kirjoitettu");
                        dataUlos.writeFloat(paketti.getNykySijainti().y);
                        dataUlos.flush();
                        Thread.sleep(50);
                        System.out.println("Nykysijainti Y kirjoitettu");
                        dataUlos.writeFloat(paketti.getUusiSijainti().x);
                        dataUlos.flush();
                        Thread.sleep(50);
                        System.out.println("UusiSijainti X kirjoitettu");
                        dataUlos.writeFloat(paketti.getUusiSijainti().y);
                        dataUlos.flush();
                        Thread.sleep(50);
                        System.out.println("UusiSijainti Y kirjoitettu");
                        dataUlos.writeFloat(paketti.getMittausSuunta().x);
                        dataUlos.flush();
                        Thread.sleep(50);
                        System.out.println("GetMittausSuunta X kirjoitettu");
                        dataUlos.writeFloat(paketti.getMittausSuunta().y);
                        dataUlos.flush();
                        Thread.sleep(50);
                        System.out.println("getMittausSuunta Y kirjoitettu");
                        kirjoitus = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        uudelleenKaynnista();
                    }
                }
                if (luku) {

                    try {
                        System.out.println("#######LUKU ALKAA#######");
                        paketti.setId(dataSisaan.readInt());
                        System.out.println("ID luettu");
                        for (int i = 0; i < BTPaketti.MAARA; i++) {
                            tempEtaisyydet[i] = dataSisaan.readInt();
                            System.out.println("Etaisyyksiä luetaan");
                        }

                        paketti.setNykySijainti(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));
                        System.out.println("Nykysijainti Luettu");
                        paketti.setUusiSijainti(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));
                        System.out.println("UusiSijaint Luettu");
                        paketti.setMittausSuunta(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));
                        System.out.println("MittausSuunta Luettu");
                        muuttunut = true;
                        
                        luku = false;
                    } catch (Exception e) {
                        // uudelleenKaynnista();
                    }
                }
            }
        }
    }

    @Override
    public BTPaketti lahetaJaVastaanota(BTPaketti paketti, int odotusAikaMs) {
        //  this.odotusAikaMS = odotusAikaMs;
        this.paketti = paketti;
        kirjoitus = true;
        while(kirjoitus){}
        
        luku = true;
        muuttunut = false;
        while (!muuttunut) {}

        return this.paketti;
    }

    /**
     * 
     */
    @Override
    public void uudelleenKaynnista() {
        alustaYhteys();
    }

    /**
     * 
     * @return
     */
    @Override
    public BTPaketti annaOletusPaketti() {
        BTPaketti p = new BTPaketti(robo.getID());
        p.setNykySijainti(new Point2D.Float(0, 0));
        p.setUusiSijainti(new Point2D.Float(0, 0));
        p.setMittausSuunta(new Point2D.Float(1, 0));
        return p;
    }
}
