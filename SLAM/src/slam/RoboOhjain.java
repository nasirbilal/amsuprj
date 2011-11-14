/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

/**
 * @brief Luokka ohjaa robottia ja käsittelee siltä tulevaa dataa.
 * 
 * Bluetooth-yhteyden avulla luokka vastaanottaa robotilta sen sijaintitiedot
 * ja kerätyt mittaustulokset. Luokka vastaa mittaustulosten yhdistämisestä
 * robotin henkilökohtaisesti kartoittamaksi kartaksi ja sen perusteella robotin
 * laskennallisen sijainnin määrittämisestä. Kaikki kommunikointi robotin kanssa
 * tapahtuu tämän luokan kautta. Jokaista robottia vastaa yksi RoboOhjain-
 * loukan ilmentymä.
 */
public class RoboOhjain extends Thread {

    private volatile boolean stop = false;
    private BTYhteys bt = null;
    private JsimData data;
    private Mittaustulokset sailio;
    private JsimRobo robo;

    RoboOhjain(BTYhteys bt,JsimRobo robo,JsimData data, Mittaustulokset sailio) {
        this.bt = bt;
        this.data = data;
        this.sailio = sailio;
        this.robo = robo;
    }

    @Override
    public void run(){
        while (!stop) {
            data = robo.valitseUusiPiste();
            sailio.uusiMittaus(data);
        }
    }

    public void lopeta() { stop = true; }
}
