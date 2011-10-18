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
        
        Mittaukset testimitat;
        float tinters[] = new float[36];
        
        RoboSimulaattori Jantunen = new RoboSimulaattori();
        
        System.out.println("Alotuspaikka: (" + Jantunen.getX() + "," + Jantunen.getY() + ")");
        System.out.println("Alotussuunta on: " + Jantunen.getSuunta());
        
        System.out.println("Kuljetaas eteenpäin 70 cm");
        Jantunen.etene(70);
        System.out.println("Paikka: (" + Jantunen.getX() + "," + Jantunen.getY() + ")");
        System.out.println("Suunta on: " + Jantunen.getSuunta());
        
        System.out.println("Testi intersect");
        //Nykyisillä kartoilla erittäin epätodennäköstä saada mitään siihen näkökenttään
        testimitat = Jantunen.mittaa();
        
        
        //Seuraavan homman pitäis tulostaa mittauksesta tiedot mutta tuloksissa ei ole mitään järkee
        tinters = testimitat.getTaulu();
        int suunta = -90;
        for (int i=0; i<tinters.length; i++){
            
            System.out.println("suunta:" + suunta + " pituus" + tinters[i]);
            suunta = suunta + 5;
        }
        
    }
}
