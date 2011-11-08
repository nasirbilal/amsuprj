
package slam;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio
 */
public class JsimKartta {
    
    private Line2D.Float kartta[];
    
    public JsimKartta(){
        kartta = new Line2D.Float[20];
        asetaKartta();
    }
    
    private void asetaKartta(){
                //testikartta
                //käytännössä 4 samanlaista lootaa jokasessa pääilmansuunnassa
        
        kartta[0] = new Line2D.Float(new Point2D.Float(500,50), new Point2D.Float(550,50));
        kartta[1] = new Line2D.Float(new Point2D.Float(550,50), new Point2D.Float(550,-50));
        kartta[2] = new Line2D.Float(new Point2D.Float(550,-50), new Point2D.Float(500,-50));
        kartta[3] = new Line2D.Float(new Point2D.Float(500,-50), new Point2D.Float(500,50));
        
        kartta[4] = new Line2D.Float(new Point2D.Float(-50,-500), new Point2D.Float(50,-500));
        kartta[5] = new Line2D.Float(new Point2D.Float(50,-500), new Point2D.Float(50,-550));
        kartta[6] = new Line2D.Float(new Point2D.Float(50,-550), new Point2D.Float(-50,-550));
        kartta[7] = new Line2D.Float(new Point2D.Float(-50,-550), new Point2D.Float(-50,-500));
        
        kartta[8] = new Line2D.Float(new Point2D.Float(-500,50), new Point2D.Float(-500,-50));
        kartta[9] = new Line2D.Float(new Point2D.Float(-500,-50), new Point2D.Float(-550,-50));
        kartta[10] = new Line2D.Float(new Point2D.Float(-550,-50), new Point2D.Float(-550,50));
        kartta[11] = new Line2D.Float(new Point2D.Float(-550,50), new Point2D.Float(-500,50));
        
        kartta[12] = new Line2D.Float(new Point2D.Float(-50,500), new Point2D.Float(-50,550));
        kartta[13] = new Line2D.Float(new Point2D.Float(-50,550), new Point2D.Float(50,550));
        kartta[14] = new Line2D.Float(new Point2D.Float(50,550), new Point2D.Float(50,500));
        kartta[15] = new Line2D.Float(new Point2D.Float(50,500), new Point2D.Float(-50,500));
        
        kartta[16] = new Line2D.Float(new Point2D.Float(-300,300), new Point2D.Float(-300,350));
        kartta[17] = new Line2D.Float(new Point2D.Float(-300,350), new Point2D.Float(-350,350));
        kartta[18] = new Line2D.Float(new Point2D.Float(-350,350), new Point2D.Float(-350,300));
        kartta[19] = new Line2D.Float(new Point2D.Float(-350,300), new Point2D.Float(-300,300));
        
        
        
        // kartta[16] = new Line2D.Float(new Point2D.Float(-50,500), new Point2D.Float(50,500));
        //kartta[4] = new Line2D.Float(new Point2D.Float(10,300), new Point2D.Float(50,300));
    }
    
    public Line2D.Float[] getKartta(){
        return kartta;
    }
    
}
