
package slam;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author juho
 */
public class JsimTesti {
    
    public static void main(String[] args) {
    
        JsimRobo jantunen = new JsimRobo(0,0,0);
        JsimKartta kartta = new JsimKartta();
        
        Point2D.Float paikka = jantunen.getPaikka();
        System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
        System.out.println("robotin suunta on: " + jantunen.getSuunta());
        System.out.print("mennäänpä 500mm eteenpäin -> ");
        jantunen.etene(500);
        paikka = jantunen.getPaikka();
        System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
        System.out.println("robotin suunta on: " + jantunen.getSuunta());
        System.out.print("käännytään vasemmalle ->");
        paikka = jantunen.getPaikka();
        System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
        System.out.println("robotin suunta on: " + jantunen.käänny(-90));
        System.out.print("käännytään uudestaan vasemmalle ->");
        paikka = jantunen.getPaikka();
        System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
        System.out.println("robotin suunta on: " + jantunen.käänny(-90));
        System.out.print("mennäänpä 500mm eteenpäin -> ");
        jantunen.etene(500);
        paikka = jantunen.getPaikka();
        System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
        System.out.println("robotin suunta on: " + jantunen.getSuunta());
        
    }
    
}
