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
    
    public Kerailija(JsimRobo robo, Mittaustulokset mitat){
        this.robo=robo;
        this.sailio=mitat;
    }

    
    @Override
    public void run(){
        while(!valmis){
            // Mitataan ja tallennetaan mittaukset säiliöön
            data = robo.valitseUusiPiste(); //robotti valitsee uuden pisteen ja liikkuu sinne ja palauttaa etäisyydet
            sailio.uusiMittaus(data);
            try{ 
                sleep(10000);
            }catch(Exception e){}
        }
    }
    
    public void lopeta(){
        valmis = true;
    }
    
    public laskePisteet(){
        
    }
    
}


