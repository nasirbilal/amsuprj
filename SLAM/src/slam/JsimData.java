
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
    
        /**
         * @param parametri robotin suunta
         * @param parametri mittaus-taulukko
         */
    public JsimData(float suunta, float[] data){
        robosuunta = suunta;
        this.data = data;
    }
    
    /**
     * @return robotin suunnan tältä mittaukselta
     */
    public float getRobosuunta(){
        return robosuunta;
    }

    /**
     * @return robotin luoman data-taulukon
     */
    public float[] getData(){
        return data;
    }
    
}
