package slam;

import java.awt.geom.Point2D;
import java.io.*;
import lejos.pc.comm.*;

/**
 *
 * @author Olli Koskinen
 */
public class NXTBTYhteys extends BTYhteys {

    private volatile boolean jatkuu;
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
    private final UI ui;

    /**
     * 
     * @param robo
     */
    public NXTBTYhteys(UI ui) {
        this.yrityksia = 0;
        this.robo = new JsimRobo();
        this.paketti = new BTPaketti(robo.getID());                            //BTPaketti
        // this.odotusAikaMS = 5;                          //"Connection timeout" -EI IMPLEMENTOITU
        this.yhteys = new NXTConnector();
        this.nimi = robo.getNimi();
        this.jatkuu = true;
        this.luku = false;
        this.kirjoitus = false;
        this.muuttunut = false;
        this.ui = ui;

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
                ui.asetaDebugTeksti("!!!!!!!!!!!!!!!Yhdistaminen epaonnistui!!!!!!!!!!!!!!!!!!!");
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
                        ui.asetaDebugTeksti("");
                        ui.asetaDebugTeksti("#######KIRJOITUS ALKAA#######");
                        dataUlos.writeInt(paketti.getId());
                        dataUlos.flush();
                       // Thread.sleep(50);
                       // ui.asetaDebugTeksti("ID kirjoitettu");
                        dataUlos.writeFloat(paketti.getNykySijainti().x);
                        dataUlos.flush();
                        //Thread.sleep(50);
                        //ui.asetaDebugTeksti("Nykysijainti X kirjoitettu");
                        dataUlos.writeFloat(paketti.getNykySijainti().y);
                        dataUlos.flush();
                        //Thread.sleep(50);
                       // ui.asetaDebugTeksti("Nykysijainti Y kirjoitettu");
                        dataUlos.writeFloat(paketti.getUusiSijainti().x);
                        dataUlos.flush();
                        //Thread.sleep(50);
                       // ui.asetaDebugTeksti("UusiSijainti X kirjoitettu");
                        dataUlos.writeFloat(paketti.getUusiSijainti().y);
                        dataUlos.flush();
                        //Thread.sleep(50);
                       // ui.asetaDebugTeksti("UusiSijainti Y kirjoitettu");
                        dataUlos.writeFloat(paketti.getMittausSuunta().x);
                        dataUlos.flush();
                       // Thread.sleep(50);
                        //ui.asetaDebugTeksti("GetMittausSuunta X kirjoitettu");
                        dataUlos.writeFloat(paketti.getMittausSuunta().y);
                        dataUlos.flush();
                        //Thread.sleep(50);
                        //ui.asetaDebugTeksti("getMittausSuunta Y kirjoitettu");
                        kirjoitus = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        uudelleenKaynnista();
                    }
                }
                if (luku) {

                    try {
                        ui.asetaDebugTeksti("#######LUKU ALKAA#######");
                        paketti.setId(dataSisaan.readInt());
                        String tulosTeksti = "";
                        int[] tempEtaisyydet = new int[paketti.getEtaisyydet().length];

                        for (int i = 0; i < BTPaketti.MAARA; i++) {
                            tempEtaisyydet[i] = dataSisaan.readInt();
                            tulosTeksti += tempEtaisyydet[i] + ", ";
                        }
                        paketti.setEtaisyydet(tempEtaisyydet);
                        ui.asetaDebugTeksti("Robo "+paketti.getId()+": " + tulosTeksti);

                        paketti.setNykySijainti(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));
                        paketti.setUusiSijainti(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));
                        paketti.setMittausSuunta(new Point2D.Float(dataSisaan.readFloat(), dataSisaan.readFloat()));
                        ui.asetaDebugTeksti("Robo " + paketti.getId() + ": " +
                            paketti.getNykySijainti().x + "," + paketti.getNykySijainti().y +
                             " -> " + paketti.getUusiSijainti().x + "," + paketti.getUusiSijainti().y +
                             " @ " + paketti.getMittausSuunta());
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
        while (kirjoitus) {}

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