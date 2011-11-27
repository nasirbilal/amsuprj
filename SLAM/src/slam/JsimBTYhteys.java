package slam;

/**
 *
 * @author Mudi
 */
public class JsimBTYhteys implements BTYhteys {

    private JsimRobo robo;

    public JsimBTYhteys() {
        this.robo = new JsimRobo();
    }

    public int getRoboID() { return robo != null ? robo.getID() : -1; }

    @Override
    public BTPaketti lahetaJaVastaanota(BTPaketti paketti, int odotusAikaMs) {
        // Lähetä data robotille ja odota sen vastausta.
        robo.setPaikka(paketti.getNykySijainti());
        robo.etenePisteeseen(paketti.getUusiSijainti());
        robo.käännyKohti(paketti.getMittausSuunta());
        
        // Robotti suorittaa mittauksia...
        float[] mittaukset = robo.mittaa(paketti.getEtaisyydet().length);
        int[] etäisyydet = new int[paketti.getEtaisyydet().length];
        for (int i = 0; i < mittaukset.length; ++i)
            etäisyydet[i] = (int)(mittaukset[i] + 0.5f); // data valmiiksi milleissä.
        
        // Tässä "robotti kirjoittaa pakettiin omat sijaintiarvionsa."
        paketti.setNykySijainti(paketti.getUusiSijainti());
        paketti.setEtaisyydet(etäisyydet);
        
        // Bluetooth-yhteys on hidas. Luo vähän viivettä datasiirtoon.
        try { Thread.currentThread().sleep(50 + (long)(Math.random()* 200));
        } catch (InterruptedException ex) { }
        
        return paketti; // Palauta "BT:n yli tullut" robotin vastauspaketti.
    }

    @Override
    public void uudelleenKaynnista() {
        this.robo = new JsimRobo();
    }
}
