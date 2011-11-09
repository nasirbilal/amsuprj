/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.util.ArrayList;

/**
 *
 * @author K1
 * 
 * 
 * Säiliöluokka, joka sisältää robotin antamat mittaustulokset JsimData-olioina.
 * JsimData sisältää robotin suunnan floattina, float taulukon robotin mittaamista etäisyyksistä ja robotin sijainnin.
 * 
 */
public class Mittaustulokset {
    private ArrayList<JsimData> mittaukset = new ArrayList<JsimData>();
    
    public synchronized void uusiMittaus(JsimData uusi){
        mittaukset.add(uusi);
    }
    
    public void tulosta(){
        for(JsimData j : mittaukset){
           System.out.print("Suunta " +  j.getRobosuunta() + " ");
           System.out.print("Paikka " + j.getPaikka() + " ");
          float taulu[] = j.getData();
            for(Float f : taulu){
                System.out.print("****");
                System.out.println(f);
            }
        }
    }
}
