/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.io.Serializable;

/**
 *
 * @author Mudi
 */
//Kohteen ID: int id
//				Jos bluetooth-viestin siirto on broadcast-tyyppinen
//			Robotin sijainti: java.awt.Point
//				Tietokoneelta robotin suuntaan siirrettäessä merkitsee robotin siirtymää eteenpäin ja
//					vaakasuuntaan nykyisen katsomissuunnan suhteen.
//					Tämä siksi, että robotin sisäisen kirjanpidon mukainen sijainti voi olla eri
//					kuin havaintojen perusteella laskettu sijainti, jolloin robotille pitäisi
//					lähettää joko siirtymä tai sitten sekä todellinen sijainti ETTÄ uusi sijainti.
//			Robotin suunta : float angle
//				Katsomissuunta asteina, arvot [0, 360[.
//				Tietokoneelta robotin suuntaan siirrettäessä arvo 0.
//				Kääntymän määrä lasketaan kaavalla rot = (rotM + rotC1 - rotC0) / 2, jossa
//					rotM = robotin kääntyessä moottoreista mitattu kääntymisen määrä,
//					rotC0 = robotin kompassin antama suunta ennen kääntymistä,
//					rotC1 = robotin kompassin antama suunta kääntymisen jälkeen.
//				Palautettava arvo lasketaan kaavalla angle = rotC0 + rot.
//				Jos robotissa ei ole kompassia, rotC0 = rotC1 = 0.
//				Tietokoneelta robotin suuntaan siirrettäessä merkitsee suuntaa, josta robotin pitäisi
//					suorittaa uudet mittaukset.
public class BTPaketti implements Serializable {

    private int[] mittaukset;
    private Point2D.Float sijaiti;
    private float suunta;
    private int id;

    public BTPaketti(float suunta, int id) {
        this.suunta = suunta;
        this.id = id;
        this.mittaukset = new int[37];
    }

    public int[] getMittaukset() {
        return mittaukset;
    }

    public void setMittaukset(int[] mittaukset) {
        this.mittaukset = mittaukset;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getSijaiti() {
        return sijaiti;
    }

    public void setSijaiti(Float sijaiti) {
        this.sijaiti = sijaiti;
    }

    public float getSuunta() {
        return suunta;
    }

    public void setSuunta(float suunta) {
        this.suunta = suunta;
    }
}
