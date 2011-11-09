/**
 * VÄLIAIKAINEN: Poistetaan käytöstä, kun UI on hierottu kuntoon
 */

package slam;

import javax.swing.UIManager;

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
          try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {}
        Komentaja komentaja = new Komentaja();
        UI ui = new UI(komentaja);
    }
}
