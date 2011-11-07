
package slam;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio
 */



public class JsimRoboNakyma {
    
    private Line2D.Float katsetaulu[];     //(Astenäkymäviiva) Line2D.Float-olioita, jotka esittävät
                            //(simu)robotin jostain suunnasta ottamaa "näkymää".
    
    /**
     * @param paikka on robotin paikka mittaushetkellä.
     * @param  suunta on robotin suunta mittaushetkellä.
     * @param  on mittausten määrä (default 37 eli 5 asteen välein)
     * @param  on IR-sensorin kantama (default 800mm)
     */
    public JsimRoboNakyma(Point2D.Float paikka, float katsesuunta, int mittausmaara, int infraEtaisyys){
        
        /*
         * Tässä luodaan robotin näkyöviivoista robon suuntaan ja paikkaan
         * perustuen taulukko, jota vertaillaan sitten kartan viivoihin.
         * 
         * 
         * 
         */
        
        katsetaulu = new Line2D.Float[mittausmaara]; //näköviivojen taulukko
        float katsekulma = 180/(mittausmaara-1);     //kuinka suuri kulma jää katseviivojen väliin
        katsesuunta = katsesuunta - 90;              //katse vasemmalle
        
        for (int i = 0; i < katsetaulu.length; i++){
            
            //näköviivan pään X = x + (sensorin kantama) * sin (katseen suunta radiaaneina)
            float x = (float)(paikka.x + infraEtaisyys*Math.sin((katsesuunta*(Math.PI/180))));
            float y = (float)(paikka.y + infraEtaisyys*Math.cos((katsesuunta*(Math.PI/180))));
            Point2D.Float aari = new Point2D.Float(x,y);
            
            katsetaulu[i] = new Line2D.Float(paikka, aari);
            //näköviiva menee robotin nykyisestä paikasta
            //laskettuun pisteeseen
            
            katsesuunta = katsesuunta + katsekulma; //seuraavan katseen suunta
        }
        
    }
    
    public Line2D.Float getNakoviiva(int i){
        return katsetaulu[i];
    }
    public Line2D.Float[] getNakotaulu(){
        return katsetaulu;
    }
    
    
    public Point2D.Float leikkaako(Line2D.Float nakoviiva, Line2D.Float karttaviiva){
        
        /*
         * Lejossin leikkikalukoodia
         * parametreinä robotin näköviiva ja
         * viiva kartasta. palauttaa ehkä
         * leikkauspisteen, ehkä jotain muuta.
         */
        
        float x, y, a1, a2, b1, b2;
    
        if (nakoviiva.y2 == nakoviiva.y1 && karttaviiva.y2 == karttaviiva.y1) return null; // horizontal parallel
        if (nakoviiva.x2 == nakoviiva.x1 && karttaviiva.x2 == karttaviiva.x1) return null; // vertical parallel

        // Find the point of intersection of the lines extended to infinity
        if (nakoviiva.x1 == nakoviiva.x2 && karttaviiva.y1 == karttaviiva.y2) { // perpendicular
            x = nakoviiva.x1;
            y = karttaviiva.y1;
        } else if (nakoviiva.y1 == nakoviiva.y2 && karttaviiva.x1 == karttaviiva.x2) { // perpendicular
            x = karttaviiva.x1;
            y = nakoviiva.y1;
        } else if (nakoviiva.y2 == nakoviiva.y1 || karttaviiva.y2 == karttaviiva.y1) { // one line is horizontal
            a1 = (nakoviiva.y2 - nakoviiva.y1) / (nakoviiva.x2 - nakoviiva.x1);
            b1 = nakoviiva.y1 - a1 * nakoviiva.x1;
            a2 = (karttaviiva.y2 - karttaviiva.y1) / (karttaviiva.x2 - karttaviiva.x1);
            b2 = karttaviiva.y1 - a2 * karttaviiva.x1;

        if (a1 == a2) return null; // parallel
            x = (b2 - b1) / (a1 - a2);
            y = a1 * x + b1;
        } else {
            a1 = (nakoviiva.x2 - nakoviiva.x1) / (nakoviiva.y2 - nakoviiva.y1);
            b1 = nakoviiva.x1 - a1 * nakoviiva.y1;
            a2 = (karttaviiva.x2 - karttaviiva.x1) / (karttaviiva.y2 - karttaviiva.y1);
            b2 = karttaviiva.x1 - a2 * karttaviiva.y1;

        if (a1 == a2) return null; // parallel
            y = (b2 - b1) / (a1 - a2);
            x = a1 * y + b1;
        }

        Point2D.Float risteys = new Point2D.Float(x,y);

        return risteys;
    }
    
}
