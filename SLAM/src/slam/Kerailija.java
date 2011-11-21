/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

/**
 *
 * @author 
 * 
 * 
 * Keräilijä on thread-luokka joka kerää robotin mittaustuloksia
 * ja asettaa ne säiliöluokkaan
 *
 */
public class Kerailija extends Thread{
    private volatile boolean valmis = false;
    private JsimRobo robo;
    private JsimData data;
    private Mittaustulokset sailio;
    private final UIRoboNakyma robonaky;
    
    public Kerailija(JsimRobo robo, Mittaustulokset mitat,UIRoboNakyma robonaky){
        this.robo=robo;
        this.sailio=mitat;
        this.robonaky=robonaky;
    }

    
    @Override
    public void run(){
        while(!valmis){
            // Mitataan ja tallennetaan mittaukset säiliöön
            data = robo.valitseUusiPiste(37); //robotti valitsee uuden pisteen ja liikkuu sinne ja palauttaa etäisyydet
            
            sailio.uusiMittaus(data);
            System.out.println("runneri");
            
            System.out.println("r.ep(d.gid); "+robonaky.etaisyydetPisteiksi(data.getIntData()));
            System.out.println("d.gid: "+data.getIntData());
            robonaky.piirraEtaisyydet(robonaky.etaisyydetPisteiksi(data.getIntData()));
            int[] lol =data.getIntData();
            System.out.println("getintdata: "+lol[0]);
            System.out.println("runneri");
            try{ 
                sleep(10000);
            }catch(Exception e){}
        }
    }
    
    public void lopeta(){
        valmis = true;
    }
}
