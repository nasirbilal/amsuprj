
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
     * Palauttaa datan pisteinä. Jos pistettä ei ole,
     * palauttaa pisteen (666666,666666)
     * 
     * @return data Point2D.Float olioina
     */
    public Point2D.Float[] getPisteet(){
        
        Point2D.Float[] datapisteet = new Point2D.Float[data.length];
        
        for (int i = 0; i < data.length; i++){
            
            if (data[i] != 9999){
            
                if ( (robosuunta + (i*5) - 90) == -270 ){   //W //länsi
                    float x = paikka.x - data[i];
                    float y = paikka.y;
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) == 0 ){   //N //pohjoinen
                    float x = paikka.x;
                    float y = paikka.y + data[i];
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) < 90 ){   //NE //koilinen
                    float x = (float)(paikka.x + data[i]*Math.sin( (robosuunta + (i*5) - 90)*(Math.PI/180) )) ;
                    float y = (float)(paikka.y + data[i]*Math.cos( (robosuunta + (i*5) - 90)*(Math.PI/180) )) ;
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) == 90 ){  //E //itä
                    float x = paikka.x + data[i];
                    float y = paikka.y;
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) < 180 ){ //SE //kaakko
                    float x = (float)(paikka.x + data[i]*Math.sin( (180 - (robosuunta + (i*5) - 90))*(Math.PI/180) )) ;
                    float y = (float)(paikka.y - data[i]*Math.cos( (180 - (robosuunta + (i*5) - 90))*(Math.PI/180) )) ;
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) == 180 ){ //S //etelä
                    float x = paikka.x;
                    float y = paikka.y - data[i];
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) < 270 ){  //SW //lounas omnomnom
                    float x = (float)(paikka.x - data[i]*Math.sin( ((-180 + robosuunta + (i*5) - 90))*(Math.PI/180) )) ;
                    float y = (float)(paikka.y - data[i]*Math.cos( ((-180 + robosuunta + (i*5) - 90))*(Math.PI/180) )) ;
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) == 270 ){ //W //länsi
                    float x = paikka.x - data[i];
                    float y = paikka.y;
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) < 360 ){  //NW //luode
                    float x = (float)(paikka.x - data[i]*Math.sin( ((360 - robosuunta + (i*5) - 90))*(Math.PI/180) )) ;
                    float y = (float)(paikka.y + data[i]*Math.cos( ((360 - robosuunta + (i*5) - 90))*(Math.PI/180) )) ;
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) == 360 ){ //N //pohjoinen
                    float x = paikka.x;
                    float y = paikka.y + data[i];
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) < 450 ){  //NW //koilinen
                    float x = (float)(paikka.x + data[i]*Math.sin( ((-360 + robosuunta + (i*5) - 90))*(Math.PI/180) )) ;
                    float y = (float)(paikka.y + data[i]*Math.cos( ((-360 + robosuunta + (i*5) - 90))*(Math.PI/180) )) ;
                    datapisteet[i] = new Point2D.Float(x,y);
                } else if ( (robosuunta + (i*5) - 90) == 450 ){ //E //itä
                    float x = paikka.x + data[i];
                    float y = paikka.y;
                    datapisteet[i] = new Point2D.Float(x,y);
                }
            } else {
                datapisteet[i] = new Point2D.Float(666666,666666);//ei pistettä
            }

        }
        return datapisteet;
    }
}
