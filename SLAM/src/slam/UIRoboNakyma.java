/**
 * Luokka jolla toteutetaan eri roboottien näkymät käyttöliittymässä.
 */
package slam;

import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.*;

/**
 *
 * @author Olli Koskinen
 */
public class UIRoboNakyma extends JPanel {

    private Point2D.Double[] pisteet;
    Point2D.Double sailio;

    /**
     * 
     */
    public UIRoboNakyma() {
        this.pisteet = null;
        final float katko[] = {10.0f};
        final BasicStroke katkoV = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, katko, 0.0f);
    }

    
    @Override
    public void paintComponent(Graphics g) {

        final float katko[] = {10.0f};
        final BasicStroke katkoV = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, katko, 0.0f);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        super.paintComponent(g);
        //Reunukset
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        //Piirretään viiva pisteiden välillä ja lopuksi vielä viivat
        // 'origosta' ensimmäiseen ja viimeiseen pisteeseen
        // tämä siis simuloi aluetta, jonka robotti näkee
        if (pisteet != null) {
            Point2D.Double temp = null, ensimmainen = null, viimeinen = null;
            for (Point2D.Double piste : pisteet) {
                if (ensimmainen == null) {
                    ensimmainen = piste;
                }
                viimeinen = piste;

                if (temp != null) {
                    g2.drawLine((int)temp.x, (int)temp.y, (int)piste.x, (int)piste.y);
                }
                temp = piste;
            }
            if (ensimmainen == null) {
                JOptionPane.showMessageDialog(this, "Point-ensimmäinen is null");
            }
            g2.setStroke(katkoV);
            g2.setColor(Color.red);
            //Pitää olla int,int,int,int ; ei int,int,double,double, tämä vain temp korjaus
            g2.drawLine((getWidth()/2), getHeight(), (int)ensimmainen.x, (int)ensimmainen.y);
            g2.drawLine((getWidth()/2), getHeight(), (int)viimeinen.x, (int)viimeinen.y);
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.black);
        }
    }
    
    /**
     * 
     * @param pisteet
     */
    //TODO: pisteet etäisyyksistä
    public void piirraEtaisyydet(Point2D.Double[] pisteet) {
        
        System.out.println("piirraetaisyydet");
        System.out.println(pisteet[0]);
        this.pisteet = pisteet;
        repaint();
    }
    
    public Point2D.Double[] etaisyydetPisteiksi(int etaisyydet[]){
        
        Point2D.Double[] pistetaulu = new Point2D.Double[etaisyydet.length];

        double x;
        double y;
        System.out.println("etaisyydetpisteiks");
        for (int i = 0; i < etaisyydet.length; i++){
            
            if (etaisyydet[i] < 790){
             //   if (i == 0){
                    sailio = new Point2D.Double(-etaisyydet[i]/4, 0);
                } else if (i < 36){
                    x = (etaisyydet[i]*Math.sin(((i*5)-90)*(Math.PI/180)))/4;
                    y = (etaisyydet[i]*Math.cos(((i*5)-90)*(Math.PI/180)))/4;
                    sailio = new Point2D.Double(x,y);
                } else { //if (i == 36)
                    sailio = new Point2D.Double(etaisyydet[i]/4, 0);
                }
                pistetaulu[i]=sailio;
                System.out.println(sailio);
            //} else {
                //lol
             //   System.out.println("!!!!!NULL!!!!!");
           // }
        }
        
        return pistetaulu;
    }
    
}
