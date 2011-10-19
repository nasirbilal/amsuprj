
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
        
        System.out.println("1. ongelmia etene():ssä(//WANHA)");
        System.out.println("2. teleport-versio(//WANHA)");
        System.out.println("3. leikkaako()-testi(//WANHA)");
        System.out.println("4. etene()-testi(//WANHA)");
        System.out.println("5. Math.* testaus(//WANHA)");
        System.out.println("6. radianisoidut koodit testi");
        System.out.println("7. etenePisteeseen()-testi");
        int valinta = s.nextInt();
        
        if (valinta == 1){
        
            Point2D.Float paikka = jantunen.getPaikka();
            System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            System.out.println("mennäänpä 500mm eteenpäin -> ");
            jantunen.etene(500);
            paikka = jantunen.getPaikka();
            System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            System.out.println("käännytään vasemmalle ->");
            paikka = jantunen.getPaikka();
            System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.käänny(-90));
            System.out.println("käännytään uudestaan vasemmalle ->");
            paikka = jantunen.getPaikka();
            System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.käänny(-90));
            System.out.println("mennäänpä 500mm eteenpäin -> ");
            
            System.out.println("Robon paikka ENNEN liikkumista: (" + jantunen.getPaikka().x + "," + jantunen.getPaikka().y + ")");
            System.out.println("Robon suunta ENNEN liikkumista: " + jantunen.getSuunta());
            
            System.out.println("testataan etene()metodin funktioita:"); //Mitä ???
            System.out.println("x="+(jantunen.getPaikka().x + 500*Math.sin(jantunen.getSuunta())));
            System.out.println("y="+(jantunen.getPaikka().y + 500*Math.cos(jantunen.getSuunta())));
            
            jantunen.etene(500);    //Tässä tapahtuu jotain outoa

            /*
             * toi funktio on laskimella testattu ja se antaa pisteen arvoiksi
             * -400,58 ja 200,77 vaikka pitäs tulla 0 ja 0
             * 
             * OLLIIIII TUU PYYHKIMÄÄN!
             * PROBLEM SOLVED!
             * olli ei tullu pyyhkimään niin vetäsin hihaan
             */

            System.out.println("Robon paikka liikkumisen JÄLKEEN: (" + jantunen.getPaikka().x + "," + jantunen.getPaikka().y + ")");
            System.out.println("Robon suunta liikkumisen JÄLKEEN: " + jantunen.getSuunta());
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
            
            System.out.print("käännytään vasemmalle x2 ->");
            System.out.println("robotin suunta on: " + jantunen.käänny(-90));
            System.out.println("robotin suunta on: " + jantunen.käänny(-90));
            paikka = jantunen.getPaikka();
            System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
            System.out.println("robotin suunta on: " + jantunen.getSuunta());
            
            System.out.println("alkupisteessä ollaan!");
            /* mittaa muutettu privateksi
            System.out.println("otetaan mittaus ja rukoillaan:");
            JsimData testidata = jantunen.mittaa(kartta);
            float taulu[] = testidata.getData();
            System.out.println("mikä oli suunta testimittauksessa? se oli: " + testidata.getRobosuunta());
            System.out.println("tarkastellaanpa dataa: ");
            
            for (int i = 0; i < 37; i++){
                System.out.println("datakulma=" + i + "||lähin seinä=" + taulu[i]);
            }
            
             * Outoa dataa taas.
             */
            
            
            
        } else if (valinta == 3){ // elikkä leikkaako() toimii niin kuin pitääkin
            /* suorakulmaleikkaus toimii
            JsimRoboNäkymä testijsrn = new JsimRoboNäkymä(new Point2D.Float(0,0), 0, 37, 800);
            Point2D.Float testipiste = testijsrn.leikkaako(new Line2D.Float(0,-100,0,100),new Line2D.Float(-100,0,100,0));
            
            System.out.println("x:"+testipiste.x);
            System.out.println("y:"+testipiste.y);
            */
            
            //Näyttää leikkauspisteetkin toimivan
            JsimRoboNäkymä testijsrn = new JsimRoboNäkymä(new Point2D.Float(0,0), 0, 37, 800);
            Point2D.Float testipiste = testijsrn.leikkaako(new Line2D.Float(-5,-100,0,100),new Line2D.Float(-100,0,100,0));
            
            System.out.println("x:"+testipiste.x);
            System.out.println("y:"+testipiste.y);
            
        } else if (valinta == 4){
            
            System.out.println("etene()testi");
            
            System.out.println("jantunen.etene()");
            
            System.out.println("paikka=(" + jantunen.getPaikka().x + "," + jantunen.getPaikka().y + ")");
            System.out.println("suunta=(" + jantunen.getSuunta() + ")");
            
            //etene(){
            System.out.println("etene():n funktiot gettereillä:");
            System.out.println("x="+(jantunen.getPaikka().x + 500*Math.sin(jantunen.getSuunta())));
            System.out.println("y="+(jantunen.getPaikka().y + 500*Math.cos(jantunen.getSuunta())));
            //}
            System.out.println("etene():n funktiot arvoilla:");
            System.out.println("x="+(0 + 500*Math.sin(0)));
            System.out.println("y="+(0 + 500*Math.cos(0)));
            
            jantunen.etene(500);
            
            System.out.println("x=" + jantunen.getPaikka().x);
            System.out.println("y=" + jantunen.getPaikka().y);
            
            System.out.println("OK.");
            System.out.println("--------------");
            
            System.out.println("käänny()testi");
            
            jantunen.käänny(90);
            System.out.println("suunta: " + jantunen.getSuunta());
            jantunen.käänny(90);
            System.out.println("suunta: " + jantunen.getSuunta());
            
            System.out.println("OK.");
            System.out.println("--------------");
            
            System.out.println("etene()testi");
            
            System.out.println("jantunen.etene()");
            
            System.out.println("paikka=(" + jantunen.getPaikka().x + "," + jantunen.getPaikka().y + ")");
            System.out.println("suunta=(" + jantunen.getSuunta() + ")");
            
            //etene(){
            System.out.println("etene():n funktiot gettereillä:");  //ei toimi
            System.out.println("x="+(jantunen.getPaikka().x + 500*Math.sin(jantunen.getSuunta())));
            System.out.println("y="+(jantunen.getPaikka().y + 500*Math.cos(jantunen.getSuunta())));
            //}
            System.out.println("etene():n funktiot arvoilla:"); //ei toimi
            System.out.println("x="+(0 + 500*Math.sin(180)));   //MITÄ ***
            System.out.println("y="+(500 + 500*Math.cos(180))); //MITÄ ***
            
            //testaus
            System.out.println("sin(180)="+Math.sin(180));// EI JUMALAUTA
            System.out.println("cos(180)"+Math.cos(180));
            
            
            
            jantunen.etene(500);
            
            System.out.println("x=" + jantunen.getPaikka().x);
            System.out.println("y=" + jantunen.getPaikka().y);
            
            
        } else if (valinta == 5){
            
            System.out.println("sin(0)="+Math.sin(0) + "///////////Pitäis olla: 0");
            System.out.println("sin(10)="+Math.sin(10) + "///////////Pitäis olla: 0.174");
            System.out.println("sin(20)="+Math.sin(20) + "///////////Pitäis olla: 0.342");
            System.out.println("sin(30)="+Math.sin(30) + "///////////Pitäis olla: 0.5");
            System.out.println("sin(40)="+Math.sin(40) + "///////////Pitäis olla: 0.643");
            System.out.println("sin(50)="+Math.sin(50) + "///////////Pitäis olla: 0.766");
            System.out.println("sin(60)="+Math.sin(60) + "///////////Pitäis olla: 0.866");
            System.out.println("sin(70)="+Math.sin(70) + "///////////Pitäis olla: 0.940");
            System.out.println("sin(80)="+Math.sin(80) + "///////////Pitäis olla: 0.985");
            System.out.println("sin(90)="+Math.sin(90) + "///////////Pitäis olla: 1");
            System.out.println("sin(100)="+Math.sin(100) + "///////////Pitäis olla: 0.985");
            System.out.println("sin(110)="+Math.sin(110) + "///////////Pitäis olla: 0.940");
            System.out.println("sin(120)="+Math.sin(120) + "///////////Pitäis olla: 0.866");
            System.out.println("sin(130)="+Math.sin(130) + "///////////Pitäis olla: 0.766");
            System.out.println("sin(140)="+Math.sin(140) + "///////////Pitäis olla: 0.643");
            System.out.println("sin(150)="+Math.sin(150) + "///////////Pitäis olla: 0.5");
            System.out.println("sin(160)="+Math.sin(160) + "///////////Pitäis olla: 0.342");
            System.out.println("sin(170)="+Math.sin(170) + "///////////Pitäis olla: 0.174");
            System.out.println("sin(180)="+Math.sin(180) + "///////////Pitäis olla: 0");
            
            System.out.println("sin(pi)="+Math.sin(Math.PI));
            
            /*
             * Siis Math.sinit tahtoo parametrinä radiaaneja, olis pitäny pikkasen tarkemmin katella
             */

        } else if (valinta == 6){
            
            System.out.println("");
            
            System.out.println("Jantusen suunta ja paikka = (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());
            
            System.out.println("Eteenpäin 500mm");
            jantunen.etene(500);
            
            System.out.println("Jantusen suunta ja paikka = (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());
            
            System.out.println("Käännytään");
            jantunen.käänny(90);
            System.out.println("Suunta: " + jantunen.getSuunta());
            System.out.println("Käännytään uudelleen");
            jantunen.käänny(90);
            System.out.println("Suunta: " + jantunen.getSuunta());
            
            System.out.println("Jantusen suunta ja paikka = (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());
            
            System.out.println("Eteenpäin 500mm");
            jantunen.etene(500);
            
            System.out.println("Jantusen suunta ja paikka = (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());
            
            System.out.println("Hienoja likiarvoja :(");
            System.out.println("OK");
            System.out.println("-------------");
            
            System.out.println("Käännytään:");
            jantunen.käänny(90);
            System.out.println("Suunta: " + jantunen.getSuunta());
            System.out.println("Käännytään uudelleen");
            jantunen.käänny(90);
            System.out.println("Suunta: " + jantunen.getSuunta());
            
            System.out.println("Käännytään toiseen suuntaa -360:");
            System.out.println("Suunta: " + jantunen.getSuunta());
            
            System.out.println("käänny() toimii. is nice");
            
            System.out.println("OK");
            System.out.println("-------------");
            /* TOIMI
            System.out.println("!!!MITTAUSTESTI!!! ken tästä käy saa kaiken toivon heittää");
            
            System.out.println("otetaan mittaus ja rukoillaan:");
            JsimData testidata = jantunen.mittaa(kartta);   //mittaa() muutettu privateksi
            float taulu[] = testidata.getData();
            System.out.println("mikä oli suunta testimittauksessa? se oli: " + testidata.getRobosuunta());
            System.out.println("tarkastellaanpa dataa: ");
            
            for (int i = 0; i < 37; i++){
                System.out.println("datakulma=" + i + "||lähin seinä=" + taulu[i]);
            }
            */
            
            System.out.println("!!!NAVIGOINTITESTI!!!");
            
            JsimData testidata = jantunen.mittaa(kartta); //testi
            
            float taulu[] = testidata.getData();
            
            for (int i = 0; i < 37; i++){
                System.out.println("datakulma=" + i + "||lähin seinä=" + taulu[i]);
            }
            
            jantunen.valitseUusiPiste(kartta);
            
            System.out.println("Jantusen suunta ja paikka = (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());
            
            System.out.println("!!!UUDESTAAN!!!");
            
            testidata = jantunen.mittaa(kartta); //testi
            
            float taulu2[] = testidata.getData();
            
            for (int i = 0; i < 37; i++){
                System.out.println("datakulma=" + i + "||lähin seinä=" + taulu2[i]);
            }
            
            jantunen.valitseUusiPiste(kartta);
            
            System.out.println("Jantusen suunta ja paikka = (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());

            
            
            
        } else if (valinta == 7){
            
            System.out.println("");
            System.out.println("etenePisteeseen()-testi");
            
            System.out.println("Jantusen suunta ja paikka = (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());
            
            System.out.println("Eteenpäin pisteeseen (0,500)");
            Point2D.Float piste = new Point2D.Float(0,500);
            jantunen.etenePisteeseen(piste);
            
            System.out.println("Jantusen suunta ja paikka = (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());
            
            System.out.println("takaisin pisteeseen (0,0)");
            piste = new Point2D.Float(0,0);
            jantunen.etenePisteeseen(piste);
            
            System.out.println("Jantusen paikka ja suunta . (" + 
                    jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
                    "), " + jantunen.getSuunta());
            
            
            
            
        }
        
        
        
    }
    
}
