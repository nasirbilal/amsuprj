/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @brief Paketti on perusyksikkö, joka kulkee tietokoneen ja robotin välillä.
 *
 *Kohteen ID: int id
 *    Jos bluetooth-viestin siirto on broadcast-tyyppinen
 *Robotin nykyinen sijainti: java.awt.Point2D.Float
 *    Robotilta tietokoneelle siirrettäessä sisältää robotin arvion omasta
 *        sijainnistaan perustuen moottoreiden liikkumaan määrään.
 *    Tietokoneelta robotille siirrettäessä sisiltää robotin laskennallisen
 *        sijainnin suhteessa robotin lähtöpisteeseen.
 *    Tämä siksi, että robotin sisäisen kirjanpidon mukainen sijainti voi olla
 *        eri kuin havaintojen perusteella laskettu sijainti, jolloin robotille
 *        pitäisi lähettää joko siirtymä tai sitten sekä todellinen sijainti
 *        ETTÄ uusi sijainti.
 *Robotin seuraava mittauspiste: java.awt.Point2D.Float
 *    Tietokoneelta robotin suuntaan siirrettäessä merkitsee robotin siirtymää
 *      eteenpäin ja oikealle nykyisen katsomissuunnan suhteen.
 *    Robotilta tietokoneelle siirrettäessä arvoilla ei ole merkitystä.
 *Robotin suunta : java.awt.Point2D.Float
 *    Piste, jota kohden robotti katsoo (kohtisuoraan).
 *    Robotti kääntyy katsomaan tätä pistettä liikuttuaan uuteen sijaintiin
 *        mutta ennen mittausten tekemistä.
 *Mittausetäisyydet: int[]
 *    Robotilta lähetettäessä sisältää mittauskulmista mitatut etäisyydet.
 *    Tietokonelta robotille lähetettäessä arvoilla ei ole merkitystä.
 *
 * @author Mudi & L
 */
public class BTPaketti implements Serializable {

    private final int MAARA = 37;
    private int id;
    private Point2D.Float nykySijaiti;
    private Point2D.Float uusiSijaiti;
    private Point2D.Float mittausSuunta;
    private int[] etaisyydet;

    public BTPaketti(int id) {
        this.id = id;
        this.etaisyydet = new int[MAARA];
    }

    public Point2D.Float getNykySijaiti() {
        return nykySijaiti;
    }

    public void setNykySijaiti(Point2D.Float nykySijaiti) {
        this.nykySijaiti = nykySijaiti;
    }

    public Point2D.Float getUusiSijaiti() {
        return uusiSijaiti;
    }

    public void setUusiSijaiti(Point2D.Float uusiSijaiti) {
        this.uusiSijaiti = uusiSijaiti;
    }

    public Point2D.Float getMittausSuunta() {
        return mittausSuunta;
    }

    public void setMittausSuunta(Point2D.Float mittausSuunta) {
        this.mittausSuunta = mittausSuunta;
    }

    public int[] getEtaisyydet() {
        return etaisyydet;
    }

    public void setEtaisyydet(int[] etaisyydet) {
        this.etaisyydet = etaisyydet;
    }
}
