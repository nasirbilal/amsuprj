/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

/**
 *
 * @author K1
 */
public class Kerailija extends Thread{
    private volatile boolean valmis = false;
    
    public void run(){
        while(!valmis){
            
        }
    }
    public void lopeta(){
        valmis = true;
    }
    
}


