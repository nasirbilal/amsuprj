
package slam;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.util.Scanner;

/**
 *
 * @author juho
 */
public class JsimTesti {
    
    public static void main(String[] args) {
    
        JsimRobo jantunen = new JsimRobo(0,0,0);
        JsimKartta kartta = new JsimKartta();
        Scanner s = new Scanner(System.in);
        
        System.out.println("1. ongelmia etene():ssä");
        System.out.println("2. teleport-versio");
        int valinta = s.nextInt();
        
        if (valinta == 1){
        
            Point2D.Float paikka = jantunen.getPaikka();
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            System.out.println("mennäänpä 500mm eteenpäin -> ");
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            jantunen.etene(500);
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            paikka = jantunen.getPaikka();
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            System.out.println("käännytään vasemmalle ->");
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            paikka = jantunen.getPaikka();
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.käänny(-90));
            System.out.println("käännytään uudestaan vasemmalle ->");
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            paikka = jantunen.getPaikka();
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.käänny(-90));
            System.out.println("mennäänpä 500mm eteenpäin -> ");
            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            System.out.println("TESTI suunta=" + jantunen.getSuunta());
            jantunen.etene(500);    //Tässä tapahtuu jotain outoa

            /*
             * toi funktio on laskimella testattu ja se antaa pisteen arvoiksi
             * -400,58 ja 200,77 vaikka pitäs tulla 0 ja 0
             * 
             * OLLIIIII TUU PYYHKIMÄÄN!
             */

            System.out.println("TESTI x=" + jantunen.getPaikka().x + ", y=" + jantunen.getPaikka().y);
            System.out.println("TESTI suunta=" + jantunen.getSuunta());
            paikka = jantunen.getPaikka();
            System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            System.out.println("no mitäs nyt kävi??");
        
        } else if (valinta == 2){
            
            Point2D.Float paikka = jantunen.getPaikka();
            System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            
            System.out.println("mennäänpä 500mm eteenpäin -> ");
            jantunen.teleport(new Point2D.Float(0,500));
            paikka = jantunen.getPaikka();
            System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            
            System.out.print("käännytään vasemmalle ->");
            System.out.println("robotin suunta on: " + jantunen.käänny(-90));
            paikka = jantunen.getPaikka();
            System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            
            System.out.print("käännytään vasemmalle ->");
            System.out.println("robotin suunta on: " + jantunen.käänny(-90));
            paikka = jantunen.getPaikka();
            System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            
            System.out.println("mennäänpä 500mm eteenpäin -> ");
            //jantunen.etene(500); //ei toimi etene() täälläkään
            jantunen.teleport(new Point2D.Float(0,0));
            paikka = jantunen.getPaikka();
            System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
        }
        
    }
    
}
