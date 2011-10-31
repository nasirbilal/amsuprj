
package slam.simuKartta;
import java.awt.geom.*;


/**
 *
 * @author Juhis
 */
public class Kartta {
    
    //rukoillaan että toimii
    private static Line2D.Float kartta[] ={ new Line2D.Float(6,6,25,25), 
                             new Line2D.Float(76,50,76,0),
                             new Line2D.Float(67,90,50,100),
                             new Line2D.Float(-20,-35,-10,-35),
                             new Line2D.Float(-40,40,-10,60),
                             new Line2D.Float(80,50,80,80),
                             new Line2D.Float(0,5,0,-15),
                             new Line2D.Float(40,-40,37,-10),
                             new Line2D.Float(0,10,-80,60),
                            };
    
    public static Line2D.Float[] getKartta(){
        
        return kartta;
        
    }

    
}
