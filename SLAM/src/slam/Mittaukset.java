
package slam;

public class Mittaukset {
    
    private float suunta;
    
    private float mittaukset[] = new float[36];
    
    public Mittaukset(float suunta, float[] mittaukset){
        this.suunta=suunta;
        this.mittaukset = mittaukset; 
    }
    
    public float[] getTaulu(){
        return mittaukset;
    }
    
    /*
    public float valitseUusiSuunta(){
        int i = 0;
        int tyhjyys = 0;
        
        while (i < mittaukset.length){
            
            if (mittaukset[i])
            
            i++;
        }
        */
  //  }

    
}
