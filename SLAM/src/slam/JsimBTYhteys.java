package slam;

/**
 *
 * @author Mudi
 */
public class JsimBTYhteys implements BTYhteys {

    private JsimRobo robo;

    public JsimBTYhteys() {
        this.robo = new JsimRobo("JsimRobo");
    }

    @Override
    public BTPaketti lahetaJaVastaanota(BTPaketti paketti, int odotusAikaMs) {
        robo.setPaikka(paketti.getNykySijaiti());
        robo.etenePisteeseen(paketti.getUusiSijaiti());
        robo.käännyKohti(paketti.getMittausSuunta());
        JsimData data = robo.mittaa(paketti.getEtaisyydet().length);
        int[] etäisyydet = new int[paketti.getEtaisyydet().length];
        for (int i = 0; i < data.getData().length; ++i) {
            etäisyydet[i] = (int) (data.getData()[i]); // data valmiiksi milleissä.
        }
        paketti.setNykySijaiti(data.getPaikka());
        paketti.setEtaisyydet(etäisyydet);
        return paketti;
    }

    @Override
    public void uudelleenKaynnista() {
        this.robo = new JsimRobo("JsimRobo");
    }
}
