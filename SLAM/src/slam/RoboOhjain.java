/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Point2D;

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
public class RoboOhjain {

    private int odotusMs; /// Kauanko BT-yhteyttä odotetaan ennen time outia.
    private BTYhteys bt; /// BT-yhteys oikeaan tai simuloituun robottiin.
   
    RoboOhjain(BTYhteys bt, int odotusMilliSek) {
        this.odotusMs = odotusMilliSek;
        this.bt = bt;
    }

    /// @brief Palauttaa robotin mittaustulokset sijoitettuna koordinaatistoon.
    ///
    /// Robotin viimeksi mittaamat etäisyydet sijoitellaan mittauskulmien
    /// perusteella karteesiseen koordinaatistoon niin, että robotin ollessa
    /// origossa ja katsoesa positiivisen Y-akselin suuntaan havaitut esteet
    /// sijaitsevat oikeassa suhteessa robottiin.
    ///
    /// @return Lista robotin suhteen sijoitelluista mittausetäisyyksistä. Jos
    ///         estettä ei havaittu eli mittausetäisyys on ääretön, kyseinen
    ///         koordinaatti on null.
    public Point2D.Float[] haeEtaisyydet() {
        return new Point2D.Float[37];
    }

    /// @brief Robotti siirretään uuteen sijaintiin ja suoritetaan mittaukset.
    ///
    /// Robotille lasketaan uusi mittauspiste ja mittaussuunta. Tiedot
    /// lähetetään BT:n kautta robotille. Metodi odottaa, että robotti saa
    /// mittaukset tehtyä ja vastaanottaa mittaustulokset BT:n kautta.
    ///
    /// @return True, jos kaikki meni hyvin ja false, jos mittaustuloksia ei
    ///         saatu odotusajan umpeuduttua
    public boolean teeMittaukset() {
        return false;
    }
}
