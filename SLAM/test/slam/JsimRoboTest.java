/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Point2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lauri
 */
public class JsimRoboTest {

    public JsimRoboTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void MEGATESTI() {

        if (Math.random() < 2.0)
            return; // Poistu tästä aivan turhasta testikokoelmasta samantien!
        
        //JsimRobo jantunen = new JsimRobo(0,0,0);
        JsimRobo jantunen = new JsimRobo(0, 800);

        System.out.println("3. leikkaako()-testi(//WANHA)(//poistettu)");
        System.out.println("4. etene()-testi(//WANHA)(//poistettu)");
        System.out.println("5. Math.* testaus(//WANHA)(//poistettu)");
        System.out.println("6. radianisoidut koodit testi(//poistettu)");
        System.out.println("7. etenePisteeseen()-testi(//poistettu)");
        System.out.println("8. kuutostestit sekottaa mun pään, pakko alottaa uus(//poistettu)");
        System.out.println("9. tezitezi(//poistettu)");
        System.out.println("10. nav-testi(//poistettu)");
        System.out.println("11. getPisteet-testi");

        int valinta = 11;
/*
        if (valinta == 3){ // elikkä leikkaako() toimii niin kuin pitääkin
        JsimRoboNäkymä testijsrn = new JsimRoboNäkymä(new Point2D.Float(0,0), 0, 37, 800);
        Point2D.Float testipiste = testijsrn.leikkaako(new Line2D.Float(0,-100,0,100),new Line2D.Float(-100,0,100,0));
        
        System.out.println("x:"+testipiste.x);
        System.out.println("y:"+testipiste.y);
        
        
        //Näyttää leikkauspisteetkin toimivan
        JsimRoboNakyma testijsrn = new JsimRoboNakyma(new Point2D.Float(0,0), 0, 37, 800);
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
        
        
         * Siis Math.sinit tahtoo parametrinä radiaaneja, olis pitäny pikkasen tarkemmin katella
        
        
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
        TOIMI
        System.out.println("!!!MITTAUSTESTI!!! ken tästä käy saa kaiken toivon heittää");
        
        System.out.println("otetaan mittaus ja rukoillaan:");
        JsimData testidata = jantunen.mittaa(kartta);   //mittaa() muutettu privateksi
        float taulu[] = testidata.getData();
        System.out.println("mikä oli suunta testimittauksessa? se oli: " + testidata.getRobosuunta());
        System.out.println("tarkastellaanpa dataa: ");
        
        for (int i = 0; i < 37; i++){
        System.out.println("datakulma=" + i + "||lähin seinä=" + taulu[i]);
        }
        
        
        System.out.println("!!!NAVIGOINTITESTI!!!");
        
        JsimData testidata = jantunen.mittaa(kartta); //testi
        
        float taulu[] = testidata.getData();
        
        for (int i = 0; i < 37; i++){
        System.out.println("datakulma=" + i + "("+(int)(jantunen.getSuunta()+((i))*5-90)+")" +"||lähin seinä=" + taulu[i]);
        }
        
        jantunen.valitseUusiPiste(kartta);
        
        System.out.println("Jantusen suunta ja paikka = (" + 
        jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
        "), " + jantunen.getSuunta());
        
        //trolololo
        
        System.out.println("!!!UUDESTAAN!!!");
        
        //testidata = jantunen.mittaa(kartta); //testi
        
        float taulu2[] = testidata.getData();
        
        for (int i = 0; i < 37; i++){
        System.out.println("datakulma=" + i + "("+(int)(jantunen.getSuunta()+((i))*5-90)+")" +"||lähin seinä=" + taulu[i]);
        }
        
        // jantunen.valitseUusiPiste(kartta);
        
        System.out.println("Jantusen suunta ja paikka = (" + 
        jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
        "), " + jantunen.getSuunta());
        
        for (int i = 0; i < 37; i++){
        System.out.println("datakulma=" + i + "("+(int)(jantunen.getSuunta()+((i))*5-90)+")" +"||lähin seinä=" + taulu[i]);
        }
        
        System.out.println("outoo settii, katotaas Jsimun robonäkymän ensimmäinen näköviiva:");
        System.out.println("("+jantunen.nakyma.getNakoviiva(0).x1+","+jantunen.nakyma.getNakoviiva(0).y1+")viiva");
        System.out.println("("+jantunen.nakyma.getNakoviiva(0).x2+","+jantunen.nakyma.getNakoviiva(0).y2+")");
        
        System.out.println("näköviiva on täysin oikeessa paikassa");
        
        System.out.println("eteenpäin 500mm ja mitataan");
        jantunen.etene(500);
        
        // testidata = jantunen.mittaa(kartta);
        float taulu3[] = testidata.getData();
        
        System.out.println("Jantusen suunta ja paikka = (" + 
        jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
        "), " + jantunen.getSuunta());
        
        System.out.println("Nyt pitäs tulla tyhjää:");
        
        for (int i = 0; i < 37; i++){
        System.out.println("datakulma=" + i + "("+(int)(jantunen.getSuunta()+((i))*5-90)+")" +"||lähin seinä=" + taulu3[i]);
        }
        
        } else if (valinta == 7){
        
        System.out.println("");
        System.out.println("etenePisteeseen()-testi");
        
        System.out.println("Jantusen suunta ja paikka = (" + 
        jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
        "), " + jantunen.getSuunta());
        
        System.out.println("Eteenpäin pisteeseen (0,500)");
        Point2D.Float piste = new Point2D.Float(0,-500);
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
        
        } else if (valinta == 8){
        
        System.out.println("Testi");
        
        System.out.println("-------------");
        System.out.println("Jantusen paikka ja suunta = (" + 
        jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
        "), " + jantunen.getSuunta());
        System.out.println("-------------");
        
        
        // JsimData data1 = jantunen.valitseUusiPiste(kartta);
        
        float taulu1[] = data1.getData();
        
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu1[i]);
        }
        
        System.out.println("-------------");
        System.out.println("Jantusen paikka ja suunta = (" + 
        jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
        "), " + jantunen.getSuunta());
        System.out.println("-------------");
        
        //  JsimData data2 = jantunen.valitseUusiPiste(kartta);
        
        float taulu2[] = data2.getData();
        
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu2[i]);
        }
        
        System.out.println("-------------");
        System.out.println("Jantusen paikka ja suunta = (" + 
        jantunen.getPaikka().x + "," + jantunen.getPaikka().y + 
        "), " + jantunen.getSuunta());
        System.out.println("-------------");
        
        } else if (valinta == 9){
        
        Point2D.Float paikka = jantunen.getPaikka();
        System.out.println("robotti on pisteessä: ("+ paikka.x + "," + paikka.y + ")");
        System.out.println("robotin suunta on: " + jantunen.getSuunta());
        System.out.println("mennäänpä 499mm eteenpäin -> ");
        jantunen.etene(499);
        paikka = jantunen.getPaikka();
        System.out.println("robotin paikka on nyt: (" + paikka.x + "," + paikka.y + ")");
        System.out.println("robotin suunta on: " + jantunen.getSuunta());
        
        //    JsimData data1 = jantunen.mittaa(kartta);
        
        float taulu1[] = data1.getData();
        
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu1[i]);
        }
        
        } else if (valinta == 10){
        
        Point2D.Float paikka = jantunen.getPaikka();
        System.out.print("***ROBON paikka("+ paikka.x + "," + paikka.y + ")");
        System.out.println("-suunta:" + jantunen.getSuunta()+"***");
        System.out.println("-------------------");
        
        
        System.out.println("valitseUusiPiste:");
        //   JsimData data1 = jantunen.valitseUusiPiste(kartta);
        paikka = jantunen.getPaikka();
        System.out.print("***ROBON paikka("+ paikka.x + "," + paikka.y + ")");
        System.out.println("-suunta:" + jantunen.getSuunta()+"***");
        System.out.println("-------------------");
        
        float taulu1[] = data1.getData();
        
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu1[i]);
        }
        System.out.println("-------------------");
        System.out.println("valitseUusiPiste:");
        //  JsimData data2 = jantunen.valitseUusiPiste(kartta);
        paikka = jantunen.getPaikka();
        System.out.print("***ROBON paikka("+ paikka.x + "," + paikka.y + ")");
        System.out.println("-suunta:" + jantunen.getSuunta()+"***");
        System.out.println("-------------------");
        
        float taulu2[] = data2.getData();
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu2[i]);
        }
        
        System.out.println("-------------------");
        System.out.println("valitseUusiPiste:");
        // JsimData data3 = jantunen.valitseUusiPiste(kartta);
        paikka = jantunen.getPaikka();
        System.out.print("***ROBON paikka("+ paikka.x + "," + paikka.y + ")");
        System.out.println("-suunta:" + jantunen.getSuunta()+"***");
        System.out.println("-------------------");
        
        float taulu3[] = data3.getData();
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu3[i]);
        }
        System.out.println("-------------------");
        
        System.out.println("valitseUusiPiste:");
        //  JsimData data4 = jantunen.valitseUusiPiste(kartta);
        paikka = jantunen.getPaikka();
        System.out.print("***ROBON paikka("+ paikka.x + "," + paikka.y + ")");
        System.out.println("-suunta:" + jantunen.getSuunta()+"***");
        System.out.println("-------------------");
        
        float taulu4[] = data4.getData();
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu4[i]);
        }
        System.out.println("-------------------");
        
        System.out.println("valitseUusiPiste:");
        // JsimData data5 = jantunen.valitseUusiPiste(kartta);
        paikka = jantunen.getPaikka();
        System.out.print("***ROBON paikka("+ paikka.x + "," + paikka.y + ")");
        System.out.println("-suunta:" + jantunen.getSuunta()+"***");
        System.out.println("-------------------");
        
        //  float taulu5[] = data5.getData();
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu5[i]);
        }
        System.out.println("-------------------");
        
        System.out.println("valitseUusiPiste:");
        // JsimData data6 = jantunen.valitseUusiPiste(kartta);
        paikka = jantunen.getPaikka();
        System.out.print("***ROBON paikka("+ paikka.x + "," + paikka.y + ")");
        System.out.println("-suunta:" + jantunen.getSuunta()+"***");
        System.out.println("-------------------");
        
        // float taulu6[] = data6.getData();
        for (int i = 0; i < 37; i++){
        System.out.println("dk=" + i + ";ak="+(int)(jantunen.getSuunta()+((i))*5-90) +";ls=" + taulu6[i]);
        }
        System.out.println("-------------------");
         */
        /*
        if (valinta == 11) {

            JsimData data1 = jantunen.mittaa(37);

            Point2D.Float ptaulu[] = data1.getPisteet();

            for (int i = 0; i < ptaulu.length; i++) {
                System.out.println("ptaulu[" + i + "]=(" + ptaulu[i].x + " , " + ptaulu[i].y + ")");
            }

            System.out.println("toimii, käännytään 180astetta");
            jantunen.käänny(180);

            JsimData data2 = jantunen.mittaa(37);

            Point2D.Float ptaulu2[] = data2.getPisteet();

            Point2D.Float paikka = jantunen.getPaikka();
            System.out.println();
            System.out.print("***ROBON paikka(" + paikka.x + "," + paikka.y + ")");
            System.out.println("-suunta:" + jantunen.getSuunta() + "***");
            System.out.println("-------------------");

            for (int i = 0; i < ptaulu.length; i++) {
                System.out.println("ptaulu2[" + i + "]=(" + ptaulu2[i].x + " , " + ptaulu2[i].y + ")");
            }
        }
*/
    }

    /**
     * Test of etenePisteeseen method, of class JsimRobo.
     */
    @Test
    public void testEtenePisteeseen() {
        System.out.println("etenePisteeseen");
        for (int i = 0; i < 100; ++i) {
            Point2D.Float kohde = new Point2D.Float(
                (float)Math.random()*1600-800, (float)Math.random()*1600-800);
            JsimRobo instance = new JsimRobo((int)(Math.random()*360), 800);
            instance.setPaikka(new Point2D.Float(0,0));
            Point2D.Float expResult = new Point2D.Float(kohde.x, kohde.y);
            Point2D.Float result = instance.etenePisteeseen(kohde);
            assertEquals("kierros " + i + ": ", expResult.x, result.x, 0.001);
            assertEquals("kierros " + i + ": ", expResult.y, result.y, 0.001);
        }
    }

    /**
     * Test of kaannyKohti method, of class JsimRobo.
     */
    @Test
    public void testkaannyKohti() {
        System.out.println("k\u00e4\u00e4nnyKohti");
        for (int i = 0; i < 360; ++i) {
            Point2D.Float kohde = new Point2D.Float(
                (float)Math.cos(Math.toRadians(i)),
                (float)Math.sin(Math.toRadians(i)));
            JsimRobo instance = new JsimRobo((int)(Math.random()*360), 800);
            instance.setPaikka(new Point2D.Float(0,0));

            float expResult = i;
            float result = instance.käännyKohti(kohde);
            assertEquals(expResult, result, 0.001);
        }
    }

    /**
     * Test of mittaa method, of class JsimRobo.
     */
    @Test
    public void testMittaa() {
        System.out.println("mittaa");

//        {
//            JsimRobo instance = new JsimRobo(135, 800); // Katse luoteeseen.
//            instance.setPaikka(new Point2D.Float(950, 650));
//            float[] expResult = {113.1f, 80.0f, 113.1f, 800.0f, 800.0f};
//            float[] result = instance.mittaa(5);
//            for (int i = 0; i < expResult.length; ++i)
//                assertEquals(expResult[i], result[i], 0.05);
//        }
        
        {
            JsimRobo instance = new JsimRobo(-45, 800); // Katse kaakkoon.
            instance.setPaikka(new Point2D.Float(500, 1500));
            float[] expResult = {707.1f, 184.0f, 170.0f, 184.0f, 183.8f,
                                 140.7f, 130.0f, 140.7f, 707.1f};
            float[] result = instance.mittaa(9);
            for (int i = 0; i < expResult.length; ++i)
                assertEquals(expResult[i], result[i], 0.05);
        }
    }
}
