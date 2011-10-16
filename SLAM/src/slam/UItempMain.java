/**
 * VÄLIAIKAINEN: Poistetaan käytöstä, kun UI on hierottu kuntoon
 */

package slam;

/**
 *
 * @author Olli Koskinen
 */
public class UItempMain {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        Komentaja komentaja = new Komentaja();
        UI ui = new UI(komentaja);
        
    }
}
