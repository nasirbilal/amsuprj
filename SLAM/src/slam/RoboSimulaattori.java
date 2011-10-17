/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;
import java.awt.geom.*;
import java.util.Random;
//import slam.simuKartta.Kartta;

/*
 * Diipadaapa herpaderp!!
 * kapitalistien koira slam.simukartta ei näköjään osallistunut cominterniin,
 * mä (Monsieur Vainio) koodaan tästä vaikka jonkun karttageneraattorin jos jaksan.
 */


/**
 *
 * @author Arnold & John
 */
public class RoboSimulaattori {
    
        Random r = new Random(); //Käytetään alkusuunnan saamiseen
    
        private float x = 0;
        private float y = 0; //Robotin sijainti
        private int suunta = r.nextInt(360); //Sattumanvarainen alkusuunta
        
        private Mittaukset mitat;
        
        //private Line2D.Float kartta[] = Kartta.getKartta();
        private Line2D.Float kartta[] = RoboSimKartta.getKartta();
        
        private Line2D.Float[] roboViivat; //Simulaattorirobotin "näköviivat" ??? trolololo
        
        public RoboSimulaattori(){
                
        }
        
        public double getX(){
            return x;
        }
        public double getY(){
            return y;
        }
        public int getSuunta(){
            return suunta;
        }
        
        //Robotin eteneminen
        // Moottoreiden käyttö sensoreina ei toimi, joten etene-metodi ainoastaan käskee robottia kulkemaan eteenpäin
        // ja reitti lasketaan sensoreiden avulla niin ettei robotti voi törmätä esteisiin. 
        void etene(int matka){
                //lasketaan koordinaattien muutokset 
                x = (float)(Math.sin(suunta)* matka + x);
                y = (float)(Math.cos(suunta)* matka + y);
        }
        //Robotin kääntyminen
        void käänny(int aste){
            //lasketaan uusi suunta robotille
            suunta = suunta + aste;
            if (suunta > 359){
                suunta = suunta - 360;
            } else if (suunta < 0){
                suunta = suunta + 360;
            }
        }
        
        public Mittaukset getMitat(){
            return mitat;
        }
        
        //Robotin mittaukset infrapunalla
        void mittaa(){
            
            roboViivat = luoRoboViivat();
            suuntimalaskuri = -90;
            int tulokset[] = new int[37];
            int lyhyinPituus;
            
            int irv = 0; //Roboviivalaskuri
            int krv = 0; //karttaviivalaskuri
            final int kartanviivat = kartta.length;  //Tämä on huono mut minkäs teet
            
            while (irv < 37){
                //System.out.println("irviluuppi:" + irv); // debug
                System.out.println("SUUNTIMA:"+suuntimalaskuri);
                lyhyinPituus = 666;
                while (krv < kartanviivat ){
                    System.out.print("/krv:" + krv); // debug
                    if (roboViivat[irv].intersectsLine(kartta[krv])){
                        System.out.println("!!INTERSEXÖN!!");
                        float aputaulu[] = new float[2];
                        aputaulu = risteys(roboViivat[irv], kartta[krv]);
                        System.out.println("paikassa : (" +  aputaulu[0] + "," + aputaulu[1]);
                        
                        if (lyhyinPituus > (int)Math.sqrt(Math.pow(aputaulu[0], 2)+Math.pow(aputaulu[1], 2))){
                        
                            lyhyinPituus = (int)Math.sqrt(Math.pow(aputaulu[0], 2)+Math.pow(aputaulu[1], 2));
                            System.out.println("Lyhyin IRVIpituus:" + lyhyinPituus);
                        }
                        tulokset[irv] = lyhyinPituus;
                    }
                    krv++;
                }
                krv = 0;
                irv++;
                suuntimalaskuri = suuntimalaskuri+5;
                System.out.println();
            }
            irv = 0;

           
           Mittaukset mitat = new Mittaukset(suunta, tulokset);
            
        }
        
        
        
        //Seuraava metodi nyysitty lejossin jutuista
        public float[] risteys(Line2D.Float roboNäkö, Line2D.Float l) {
    float x, y, a1, a2, b1, b2;
    
    
    if (roboNäkö.y2 == roboNäkö.y1 && l.y2 == l.y1) return null; // horizontal parallel
    if (roboNäkö.x2 == roboNäkö.x1 && l.x2 == l.x1) return null; // vertical parallel

    // Find the point of intersection of the lines extended to infinity
    if (roboNäkö.x1 == roboNäkö.x2 && l.y1 == l.y2) { // perpendicular
      x = roboNäkö.x1;
      y = l.y1;
    } else if (roboNäkö.y1 == roboNäkö.y2 && l.x1 == l.x2) { // perpendicular
      x = l.x1;
      y = roboNäkö.y1;
    } else if (roboNäkö.y2 == roboNäkö.y1 || l.y2 == l.y1) { // one line is horizontal
      a1 = (roboNäkö.y2 - roboNäkö.y1) / (roboNäkö.x2 - roboNäkö.x1);
      b1 = roboNäkö.y1 - a1 * roboNäkö.x1;
      a2 = (l.y2 - l.y1) / (l.x2 - l.x1);
      b2 = l.y1 - a2 * l.x1;

      if (a1 == a2) return null; // parallel
      x = (b2 - b1) / (a1 - a2);
      y = a1 * x + b1;
    } else {
      a1 = (roboNäkö.x2 - roboNäkö.x1) / (roboNäkö.y2 - roboNäkö.y1);
      b1 = roboNäkö.x1 - a1 * roboNäkö.y1;
      a2 = (l.x2 - l.x1) / (l.y2 - l.y1);
      b2 = l.x1 - a2 * l.y1;

      if (a1 == a2) return null; // parallel
      y = (b2 - b1) / (a1 - a2);
      x = a1 * y + b1;
    }
    
    float risteys[] = new float[2];
    risteys[0] = x;
    risteys[1] = y;

    return risteys;
  }
        
        //Suuntimia käytetään LuoRoboViivoissa
        //Täytyy käytää aina pareittain suuntimalaskurin takia
        int suuntimalaskuri = -90;  //Tää on taas sitä koodia jota ei ymmärrä edes kirjotettaessa
        public float Xsuuntima(){
            
            if (suuntimalaskuri > 90 ){
                suuntimalaskuri = -90;
            }
            
            int apusuunta = suunta - suuntimalaskuri;
            
            if (apusuunta < 0){
                apusuunta = apusuunta + 360;
            } else if (apusuunta > 359){
                apusuunta = apusuunta - 360;
            }
            
            return (float)(Math.cos(apusuunta)+80+x);
        }
        public float Ysuuntima(){
            int apusuunta = suunta - suuntimalaskuri;
            
            if (apusuunta < 0){
                apusuunta = apusuunta + 360;
            } else if (apusuunta > 359){
                apusuunta = apusuunta - 360;
            }
            suuntimalaskuri = suuntimalaskuri +5;
            return (float)(Math.sin(apusuunta)+80+y);
        }
        
        public Line2D.Float[] luoRoboViivat(){
            //Olen pahoillani
            Line2D.Float roboViivat[] = {new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima()),
                                         new Line2D.Float(x,y,Xsuuntima(),Ysuuntima())

            };

            return roboViivat;
            
        }
        
        
}
