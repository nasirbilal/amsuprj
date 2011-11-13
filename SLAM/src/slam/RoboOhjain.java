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

    RoboOhjain(BTYhteys bt) {
        this.bt = bt;
    }

    @Override
    public void run(){
        while (!stop) {
            data = robo.valitseUusiPiste();
            sailio.uusiMittaus(data);
        }
    }

    public void stop() { stop = true; }
}
