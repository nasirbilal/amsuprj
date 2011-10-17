/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

/**
 *
 * @author K1
 */
public class RoboSimTextSim {

    public static void main(String[] args) {
        
        RoboSimulaattori Jantunen = new RoboSimulaattori();
        
        System.out.println("Alotuspaikka: (" + Jantunen.getX() + "," + Jantunen.getY() + ")");
        System.out.println("Alotussuunta on: " + Jantunen.getSuunta());
        
        System.out.println("Kuljetaas eteenpäin 70 cm");
        Jantunen.etene(70);
        System.out.println("Paikka: (" + Jantunen.getX() + "," + Jantunen.getY() + ")");
        System.out.println("Suunta on: " + Jantunen.getSuunta());
        
        System.out.println("Testi intersect");
        //Nykyisillä kartoilla erittäin epätodennäköstä saada mitään siihen näkökenttään
        Jantunen.mittaa();
        
    }
}
