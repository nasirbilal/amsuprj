
package slam;

import java.awt.geom.Point2D.Float;
import java.awt.geom.Point2D;
import java.util.Random;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio & arska
 */

/*
 * Poistan noita vanhoja ku näitä on testailtu tarpeeks
 * 
 * Jossain on häikkää, mää en tiä missä.
 * testiohjelma 8:n pitäis tuottaa 2.
 * mittauskierroksella tilanne jossa ainakin
 * dk0 ja dk36 näyttää jotain muuta kun 9999
 * 
 * liekö onkelma kääntymisessä... ainakin toi
 * arkustangentti on aika ihme vääntöä
 * 
 * OLLILLE with love: <3<3<3<3<3<3<3 XOXO
 * puspus, nähdään loman jälkeen
 * 
 JsimRobo -luokka
	Konstruktorit
		JsimRobo() //alottaa paikaststa(0,0), suunta random
		JsimRobo(float suunta, Point2D.Float paikka)
		public JsimRobo(float suunta, int x, int y) //KÄYTÄ TÄTÄ (0,0,0)

	Liikkumismetodit
		Point2D.Float etene(float matka)
		Point2D.Float etenePisteeseen(Point2D.Float kohde)
		Point2D.Float teleport(Point2D.Float kohde)

		float käänny(float aste)
		float käännyKohti(Point2D.Float kohde, float bonusaste)
		float osoitaSuuntaan(float suunta)

	Mittausmetodit
		JsimData mittaa(JsimKartta JSKkartta)


JsimKartta -luokka //luo virtuaalikartan
	konstruktori
		JsimKartta()

	getteri
		Line2D.Float[] getKartta()

JsimData
	konstruktori
		JsimData(float suunta, float[] data)

	getterit
		float getRobosuunta()
		float[] getData()

JsimRobonäkymä // tätä luodaan JsimRobon mittaa-metodissa
	konstruktori
		JsimRoboNäkymä(Point2D.Float paikka, float suunta, int mittausmäärä, int range)

	getterit
		Line2D.Float getNäköviiva(int i)
		Line2D.Float[] getNäkötaulu()

	metodi
		Point2D.Float leikkaako(Line2D.Float näköviiva, Line2D.Float karttaviiva) //LEJOSista pöllitty
 * 
 */

public class JsimRobo {
    
    private float suunta;                   /// Robotin suunta // range: 0-359 // 360 = 0 // 0 ON POHJOINEN (tai saatta olla etelä)
    private Point2D.Float paikka;           /// Robotin paikka Point-oliona MILLIMETREISSÄ
    private Point2D.Float kohde;
    
    private final int infraKantama = 800;      /// Robotin infrapunasensorin kantama MILLIMETREISSÄ
    private final int mittausMaara = 37;    /// Robotin mittaukset per 180 astetta // 37 = 5 asteen välein
    
    JsimData mittaus;                       /// luodaan mittaa()-metodilla, käytetään seuraavan mittauspaikan valitsemiseksi
    JsimRoboNäkymä nakyma;                  /// luodaan mittaa()-metodilla, debugausta
    
    
    /*
     * Konstruktorit
     */
    
    public JsimRobo(){                      /// Peruskonstruktori
        Random r = new Random();    
        suunta = r.nextInt(360);            /// Sattumanvarainen alkusuunta
        paikka = new Point2D.Float(0,0);    /// Alkukoordinaatti 0,0
    }
    public JsimRobo(float suunta, Point2D.Float paikka){
        this.suunta = suunta;
        this.paikka = paikka;
    }
    public JsimRobo(float suunta, int x, int y){
        this.suunta = suunta;
        paikka = new Point2D.Float(x,y);
        
    }
    
    /*
     * Getterit ja setterit
     */
    
    public float getSuunta(){
        return suunta;
    }
    public void setSuunta(float suunta){
        this.suunta = suunta;
    }
    
    public Point2D.Float getPaikka(){
        return paikka;
    }
    public void setPaikka(int x, int y){
        paikka = new Point2D.Float(x,y);
    }
    
    
    
    /*
     * Liikuntametodit:
     */
    
    /**
     * @param matka on MILLIMETREISSÄ.
     * @return uuden paikan Point2D.Float-oliona.
     */
    public Point2D.Float etene(float matka){
        
        float x = (float)(paikka.x + matka*Math.sin((suunta*(Math.PI/180)))); //Mathin funktiot ottaa radiaaneja
        float y = (float)(paikka.y + matka*Math.cos((suunta*(Math.PI/180))));
        paikka = new Point2D.Float(x,y);
        return paikka;
    }

    /** 
     * @param on kohteena oleva paikka.
     * @return uuden paikan Point2D.Float-oliona.
     * @note Robotti kääntyy pistettä kohti ja "ajaa" siihen. Ei mitään pathfindingiä.
     */
    public Point2D.Float etenePisteeseen(Point2D.Float kohde){
        käännyKohti(kohde,0);
        return etene((float)Math.sqrt(Math.pow(paikka.x+kohde.x,2)+(Math.pow(paikka.y+kohde.y,2))));
    }
    
    public Point2D.Float teleport(Point2D.Float kohde){
        /*
         * Mä en hiffaa enää mitään eli nyt kusetetaan
         * (käytännössä setPaikka)
         */
        paikka = kohde;
        return paikka;
    }
    
    /** 
     * @param aste on väliltä -90 (vasemmalle) viiva 90 (oikealle).
     * @return uuden suunnan.
     */
    public float käänny(float aste){
        suunta = suunta + aste;
            if (suunta > 359){
                suunta = suunta - 360;
            } else if (suunta < 0){
                suunta = suunta + 360;
            }
        System.out.println("jantusen suunta:" + suunta);
        return suunta;
    }
    
    /** 
     * @param Point2D.Float-olio, jota kohti käännytään.
     * @param on mahdollinen lisäkääntyminen, anna 0 jos ei tarvetta
     * @return uuden suunnan.
     */
    public float käännyKohti(Point2D.Float kohde, float bonusaste){

            float aste = (float)((Math.atan((paikka.x+kohde.x)/(paikka.y+kohde.y))));
            aste = (float)(aste*(180/Math.PI)); //käännetään radiaanit asteiksi
            System.out.println("aste:" + aste); //debug
            return käänny(aste+bonusaste);
    }
    
    public float osoitaSuuntaan(float suunta){
        /*
         * teleportille kusetuspartneri
         * (käytännössä setSuunta)
         */
        this.suunta = suunta;
        return suunta;
    }
    
    
    /*
     * Seuraavan pisteen valinta
     */
    
    //TODO
    
    public JsimData valitseUusiPiste(JsimKartta JSKkartta){
        
        mittaus = mittaa(JSKkartta);            //mittaus on osa näitä navigointihommia
        float mtaulu[] = mittaus.getData();     //tiedot edessäolevasta kamasta
        
        int alkusuunta = -1;
        int tyhjyyslaskuri = 0;
        int loppusuunta;
        int tyhjyysmuisti = -1;
        int alkumuisti = -1;
        
        for (int i = 0; i < mtaulu.length; i++){
            
            if (mtaulu[i] != 9999){
                alkusuunta = i;
                tyhjyyslaskuri = 0;
                
            } else {
                tyhjyyslaskuri++;
            }
            
            if (tyhjyysmuisti < tyhjyyslaskuri){
                tyhjyysmuisti = tyhjyyslaskuri +1;
                alkumuisti = alkusuunta;
            }
            
        }
        
        System.out.println("as" + alkumuisti + " tm" + tyhjyysmuisti); //debug
        
        if (alkumuisti != -1){
            if (alkumuisti + tyhjyysmuisti < mittausMaara){
                
                float tap = mtaulu[alkumuisti];
                float tlp = mtaulu[alkumuisti+tyhjyysmuisti+1];
                System.out.println("tap"+tap);
                System.out.println("tlp"+tlp);
                System.out.println("!!!navSuurinVäli("+tap+","+alkumuisti+","+tlp+","+tyhjyysmuisti+")");
                navSuurinVäli(tap, alkumuisti, tlp, alkumuisti+tyhjyysmuisti);
            }
        }
   
        return mittaus;
    }
    
    private void navSuurinVäli(float tap, int as, float tlp, int ls){

        float x1 = (float)(paikka.x + tap*Math.sin(((suunta+(as*5-90))*(Math.PI/180))));
        float y1 = (float)(paikka.y + tap*Math.cos(((suunta+(as*5-90))*(Math.PI/180))));

        float x2 = (float)(paikka.x + tlp*Math.sin(((suunta+(ls*5-90))*(Math.PI/180))));
        float y2 = (float)(paikka.y + tlp*Math.cos(((suunta+(ls*5-90))*(Math.PI/180))));

        
        System.out.println("(" + x1 + "," + y1 + ") - (" + x2 + "," + y2 + ")");    //debug
        
        float keskipisteX = (x1+x2)/2;
        float keskipisteY = (y1+y2)/2;
        
        System.out.println("keskipiste:(" + keskipisteX + "," + keskipisteY + ")"); //debug
        
        Point2D.Float etenemiskohde = new Point2D.Float(keskipisteX,keskipisteY);
        
        System.out.println("etenepisteeseen(" + etenemiskohde.x + "," + etenemiskohde.y + ")");
        System.out.println("1jantusen sijainti nyt:(" + etenemiskohde.x + "," + etenemiskohde.y + ")");
        System.out.println("1jantusen suunta nyt:" + suunta);
        
        etenePisteeseen(etenemiskohde);
        
        System.out.println("2jantusen sijainti nyt:(" + etenemiskohde.x + "," + etenemiskohde.y + ")");
        System.out.println("2jantusen suunta nyt:" + suunta);
        
        käännyKohti(new Point2D.Float(x1,y1),90);
        
        System.out.println("3jantusen sijainti nyt:(" + etenemiskohde.x + "," + etenemiskohde.y + ")");
        System.out.println("3jantusen suunta nyt:" + suunta);
    }
    
    
    /*
     * Mittaus
     */
    
    public JsimData mittaa(JsimKartta JSKkartta){
        /*
         * Tässä olis ideana, että verrataan robotin yhtä "näköviivaa" kaikkiin kartan viivoihin vuoron perään ja jos leikkaus
         * löytyy niin tallennetaan se pieninleikkaus-muuttujaan, jota vertaillaan tuleviin leikkauspituuksiin. Sitten tallennetaan
         * pieninleikkaus tauluun ja siirrytään tarkastelemaan seuraavaa näköviivaa. Lopuksi pieninleikkaus-muuttujista muodostettu
         * taulu annetaan JsimData oliolle, johon myös tallennetaan robotin tämänhetkinen suunta.
         */
        
        /*
         * jos tää roska antaa 9999 pituudeksi niin se tarkoittaa että mitää ei ole havaittu
         * 
         */

        //JsimData mittaus; !!siirretty ylös!!
        //JsimRoboNäkymä siirretty ylös
        float taulu[] = new float[mittausMaara];    //Käytetään "mittaus"-jsimdatan luomisessa
        float pieninleikkaus;
        nakyma = new JsimRoboNäkymä(paikka, suunta, mittausMaara, infraKantama);
        
        Line2D.Float kartta[] = JSKkartta.getKartta();
        
        System.out.println("luuppaus alkaa");//debug
        
        for (int i = 0; i < nakyma.getNäkötaulu().length; i++){                 //näköviiva loop
            pieninleikkaus = 9999;
            for (int k = 0; k < kartta.length; k++){                            //karttaviiva loop
                if (nakyma.getNäköviiva(i).intersectsLine(kartta[k])){          //boolean intersect if
                    System.out.print("kartta["+k+"] leikkaa näköviiva["+i+"]");//debug
                    Point2D.Float leikkauspiste = nakyma.leikkaako(nakyma.getNäköviiva(i), kartta[k]);
                    
                    System.out.print(" paikassa("+leikkauspiste.x+","+leikkauspiste.y+"), ");
                    
                    if (pieninleikkaus > Math.sqrt(Math.pow(paikka.x-leikkauspiste.x,2)+(Math.pow(paikka.y-leikkauspiste.y,2)))){

                        pieninleikkaus = (float)Math.sqrt(Math.pow(paikka.x-leikkauspiste.x,2)+(Math.pow(paikka.y-leikkauspiste.y,2)));
                        System.out.println("pit:"+pieninleikkaus);
                    }
                    
                }
            }
                taulu[i] = pieninleikkaus;
        }
        
        mittaus = new JsimData(suunta,taulu);
        
        return mittaus;
    }
    
    
    
}
