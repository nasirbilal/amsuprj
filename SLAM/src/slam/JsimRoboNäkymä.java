
package slam;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio
 */
public class JsimRoboNäkymä {
    
    Line2D.Float anv[];     //(Astenäkymäviiva) Line2D.Float-olioita, jotka esittävät
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
            
            float x = (float)(paikka.x + 800*Math.sin(suunta));
            float y = (float)(paikka.y + 800*Math.cos(suunta));
            Point2D.Float ääri = new Point2D.Float(x,y);
            
            anv[i] = new Line2D.Float(paikka, ääri);
            
            suunta = suunta + katsekulma;
        }
        
    }
    
}
