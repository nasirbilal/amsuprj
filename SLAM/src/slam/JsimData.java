
package slam;

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
    
    public JsimData(float suunta, float[] data){
        /*
         * 1.parametri robotin suunta
         * 2.parametri mittaus-taulukko
         */
        robosuunta = suunta;
        this.data = data;
    }
    
    public float getSDS(){
        /*
         * Palauttaa robotin suunnan tältä mittaukselta
         */
        return robosuunta;
    }
    public float[] getData(){
        /*
         * Palauttaa robotin luoman data-taulukon
         */
        return data;
    }
    
}
