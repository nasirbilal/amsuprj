
package slam;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio
 */
public class JsimKartta {
    
    private Line2D.Float kartta[];
    
    public JsimKartta(){
        kartta = new Line2D.Float[4]; // MINKÄ HELVETIN TAKIA TÄÄ TOIMII VAAN KONSTRUKTORISSA
        /*
         * Tässä kartassa vaan neljä seinää alkupositiosta testaamiseen
         */
        kartta[0] = new Line2D.Float(new Point2D.Float(500,50), new Point2D.Float(500,-50));
        kartta[1] = new Line2D.Float(new Point2D.Float(-50,-500), new Point2D.Float(50,-500));
        kartta[2] = new Line2D.Float(new Point2D.Float(-500,50), new Point2D.Float(-500,-50));
        kartta[3] = new Line2D.Float(new Point2D.Float(-50,500), new Point2D.Float(50,500));
    }
    
    public Line2D.Float[] getKartta(){
        return kartta;
    }
    
}
