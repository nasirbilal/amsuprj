package slam;

import java.awt.geom.Point2D;

/**
 *
 * @author Mudi
 */
public class JsimBTYhteys extends BTYhteys {

    private JsimRobo robo;
    private BTPaketti paketti;

    /**
     * 
     */
    public JsimBTYhteys() {
        this.robo = new JsimRobo();
        this.paketti = new BTPaketti(robo.getID());
        this.paketti.setNykySijainti(robo.getPaikka());
        this.paketti.setUusiSijainti(robo.getPaikka());
        this.paketti.setMittausSuunta(new Point2D.Float(
                robo.getPaikka().x, robo.getPaikka().y));
    }

    /**
     * 
     * @return
     */
    public int getRoboID() { return robo != null ? robo.getID() : -1; }

    /**
     * 
     * @param paketti
     * @param odotusAikaMs
     * @return
     */
    @Override
    public BTPaketti lahetaJaVastaanota(BTPaketti paketti, int odotusAikaMs) {
        // Lähetä data robotille ja odota sen vastausta.
        robo.setPaikka(paketti.getNykySijainti());
        robo.etenePisteeseen(paketti.getUusiSijainti()); 
        robo.käännyKohti(paketti.getMittausSuunta());
        
        // Robotti suorittaa mittauksia...
        final float[] mittaukset = robo.mittaa(paketti.getEtaisyydet().length);
        int[] etäisyydet = new int[paketti.getEtaisyydet().length];
        for (int i = 0; i < mittaukset.length; ++i){
            etäisyydet[i] = (int)(mittaukset[i] + 0.5f); // data valmiiksi milleissä.
        }
        
        // Tässä "robotti kirjoittaa pakettiin omat sijaintiarvionsa."
        paketti.setNykySijainti(paketti.getUusiSijainti());
        paketti.setEtaisyydet(etäisyydet);

        // Bluetooth-yhteys on hidas. Luo vähän viivettä datasiirtoon.
        try { Thread.currentThread().sleep(100 + (long)(Math.random()* 0));
        } catch (InterruptedException ex) { }
        
        return paketti; // Palauta "BT:n yli tullut" robotin vastauspaketti.
    }

    /**
     * 
     */
    @Override
    public void uudelleenKaynnista() {
        this.robo = new JsimRobo();
    }
    
    /**
     * 
     * @return
     */
    @Override
    public BTPaketti annaOletusPaketti() {
        BTPaketti p = new BTPaketti(robo.getID());
        Point2D.Float katsePaikka = new Point2D.Float(robo.getPaikka().x + 1,
                                             robo.getPaikka().y);
        p.setNykySijainti(robo.getPaikka());
        p.setUusiSijainti(robo.getPaikka());
        p.setMittausSuunta(katsePaikka);
        robo.käännyKohti(katsePaikka);
        return p;
    }
}
