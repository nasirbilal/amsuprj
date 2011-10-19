
package slam;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio
 */



public class JsimRoboNäkymä {
    
    private Line2D.Float anv[];     //(Astenäkymäviiva) Line2D.Float-olioita, jotka esittävät
                            //(simu)robotin jostain suunnasta ottamaa "näkymää".
    
    public JsimRoboNäkymä(Point2D.Float paikka, float suunta, int mittausmäärä, int range){
        /* 1. parametri paikka on robotin paikka mittaushetkellä.
         * 2. parametri suunta on robotin suunta mittaushetkellä.
         * 3. parametri on mittausten määrä (default 37 eli 5 asteen välein)
         * 4. parametri on IR-sensorin kantama (default 800mm)
         */
        
        anv = new Line2D.Float[mittausmäärä]; //Mä en ymmärrä minkä takia tää nyt toimii
        float katsekulma = 180/(mittausmäärä-1);
        suunta = suunta - 90;
        
        for (int i = 0; i < anv.length; i++){
            
            float x = (float)(paikka.x + 800*Math.sin((suunta*(Math.PI/180))));
            float y = (float)(paikka.y + 800*Math.cos((suunta*(Math.PI/180))));
            Point2D.Float ääri = new Point2D.Float(x,y);
            
            anv[i] = new Line2D.Float(paikka, ääri);
            
            suunta = suunta + katsekulma;
        }
        
    }
    
    public Line2D.Float getNäköviiva(int i){
        return anv[i];
    }
    public Line2D.Float[] getNäkötaulu(){
        return anv;
    }
    
    
    public Point2D.Float leikkaako(Line2D.Float näköviiva, Line2D.Float karttaviiva){
        
        /*
         * Lejossin leikkikalukoodia
         * parametreinä robotin näköviiva ja
         * viiva kartasta. palauttaa ehkä
         * leikkauspisteen, ehkä jotain muuta.
         */
        
        float x, y, a1, a2, b1, b2;
    
    
        if (näköviiva.y2 == näköviiva.y1 && karttaviiva.y2 == karttaviiva.y1) return null; // horizontal parallel
        if (näköviiva.x2 == näköviiva.x1 && karttaviiva.x2 == karttaviiva.x1) return null; // vertical parallel

        // Find the point of intersection of the lines extended to infinity
        if (näköviiva.x1 == näköviiva.x2 && karttaviiva.y1 == karttaviiva.y2) { // perpendicular
            x = näköviiva.x1;
            y = karttaviiva.y1;
        } else if (näköviiva.y1 == näköviiva.y2 && karttaviiva.x1 == karttaviiva.x2) { // perpendicular
            x = karttaviiva.x1;
            y = näköviiva.y1;
        } else if (näköviiva.y2 == näköviiva.y1 || karttaviiva.y2 == karttaviiva.y1) { // one line is horizontal
            a1 = (näköviiva.y2 - näköviiva.y1) / (näköviiva.x2 - näköviiva.x1);
            b1 = näköviiva.y1 - a1 * näköviiva.x1;
            a2 = (karttaviiva.y2 - karttaviiva.y1) / (karttaviiva.x2 - karttaviiva.x1);
            b2 = karttaviiva.y1 - a2 * karttaviiva.x1;

        if (a1 == a2) return null; // parallel
            x = (b2 - b1) / (a1 - a2);
            y = a1 * x + b1;
        } else {
            a1 = (näköviiva.x2 - näköviiva.x1) / (näköviiva.y2 - näköviiva.y1);
            b1 = näköviiva.x1 - a1 * näköviiva.y1;
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
