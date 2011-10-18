
package slam;

import java.awt.geom.Point2D.Float;
import java.awt.geom.Point2D;
import java.util.Random;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio & arska
 */

/*
 * Poistan noita vanhoja ku näitä on testailutu tarpeeks
 */

public class JsimRobo {
    
    private float suunta;                   //Robotin suunta // range: 0-359 // 360 = 0 // 0 ON POHJOINEN (tai saatta olla etelä)
    private Point2D.Float paikka;           //Robotin paikka Point-oliona MILLIMETREISSÄ
    private Point2D.Float kohde;
    
    private final float IRkantama = 800;    //Robotin infrapunasensorin kantama MILLIMETREISSÄ
    private final int mittausmäärä = 37;    //Robotin mittaukset per 180 astetta // 37 = 5 asteen välein
    
    
    /*
     * Konstruktorit
     */
    
    public JsimRobo(){              //Peruskonstruktori
        Random r = new Random();    
        suunta = r.nextInt(360);    //Sattumanvarainen alkusuunta
        paikka = new Point2D.Float(0,0);    //Alkukoordinaatti 0,0
    }
    public JsimRobo(float suunta, Point2D.Float paikka){
        this.suunta = suunta;
        this.paikka = paikka;
    }
    public JsimRobo(float suunta, int x, int y){
        this.suunta = suunta;
        paikka = new Point2D.Float(x,y);
        
    }
    
    /*
     * Getterit ja setterit
     */
    
    public float getSuunta(){
        return suunta;
    }
    public void setSuunta(float suunta){
        this.suunta = suunta;
    }
    
    public Point2D.Float getPaikka(){
        return paikka;
    }
    public void setPaikka(int x, int y){
        paikka = new Point2D.Float(x,y);
    }
    
    
    /*
     * Liikuntametodit:
     */
    
    public Point2D.Float etene(float matka){
        /* Palauttaa uuden paikan Point2D.Float-oliona.
         * parametri matka on MILLIMETREISSÄ.
         */
        float x = (float)(paikka.x + matka*Math.sin(suunta));
        float y = (float)(paikka.y + matka*Math.cos(suunta));
        paikka = new Point2D.Float(x,y);
        return paikka;
    }
    public Point2D.Float etenePisteeseen(Point2D.Float kohde){
        /* Palauttaa uuden paikan Point2D.Float-oliona.
         * Parametri on kohteena oleva paikka.
         * HUOM. Robotti kääntyy pistettä kohti ja ajaa siihen. Ei mitään pathfindingiä.
         */
        käännyKohti(kohde);
        return etene((float)Math.sqrt(Math.pow(paikka.x+kohde.x,2)+(Math.pow(paikka.y+kohde.y,2))));
    }
    
    public float käänny(float aste){
        /* Palauttaa uuden suunnan.
         * Parametri aste on väliltä -90 (vasemmalle) viiva 90 (oikealle).
         */
        suunta = suunta + aste;
            if (suunta > 359){
                suunta = suunta - 360;
            } else if (suunta < 0){
                suunta = suunta + 360;
            }
        return suunta;
    }
    
    public float käännyKohti(Point2D.Float kohde){
        /* Palauttaa uuden suunnan.
         * Parametrinä Point2D.Float-olio, jota kohti käännytään.
         */
        float aste = (float)Math.atan((paikka.x+kohde.x)/(paikka.y+kohde.y));
        
        return käänny(aste);
    }
    
    
    /*
     * Seuraavan pisteen valinta
     */
    
    //TODO
    
    
    /*
     * Mittaus :(
     */
    
    public JsimData mittaa(){
        JsimData mittaus;
        
        
        
        return mittaus;
    }
    
    
    
}
