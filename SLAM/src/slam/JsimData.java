
package slam;
import java.awt.geom.Point2D;

/**
 *
 * @author juho vainio
 */
public class JsimData {
    
    /*
     * Robotin mittauksista luotu data
     */
    
    private float robosuunta;
    private float data[];
    private Point2D.Float paikka;
    
        /**
         * @param parametri robotin suunta
         * @param parametri mittaus-taulukko
         */
    public JsimData(float suunta, float[] data, Point2D.Float paikka){
        robosuunta = suunta;
        this.data = data;
        this.paikka = paikka;
    }
    
    /**
     * @return robotin suunnan tältä mittaukselta
     */
    public float getRobosuunta(){
        return robosuunta;
    }
    public Point2D.Float getPaikka(){
        return paikka;
    }

    /**
     * @return robotin luoman data-taulukon
     */
    public float[] getData(){
        return data;
    }
    
    /**
     * @return data Point2D.Float olioina
     */
    public Point2D.Float[] getPisteet(){
        
        Point2D.Float[] datapisteet = new Point2D.Float[data.length];
        
        for (int i = 0; i < data.length; i++){
            
            float x = (float)(paikka.x + data[i]*Math.sin( (robosuunta + (i*5) - 90)*(Math.PI/180) )) ;
            float y = (float)(paikka.y + data[i]*Math.cos( (robosuunta + (i*5) - 90)*(Math.PI/180) )) ;
            
            datapisteet[i] = new Point2D.Float(x,y);
            
        }
        
        return datapisteet;
        
    }
    
    
}
