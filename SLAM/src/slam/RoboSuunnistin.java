/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author L
 */
public class RoboSuunnistin {
    
    private boolean edettytäyteen;  //Käytetään navigointiin
    private int suunta = 0; // humiin

    //urpoilua haeUusiMittauspiste-loppupuolen häkkyrään:
    private Point2D.Float vanhapiste = new Point2D.Float(0,0);
    private int i = 0;    
    private int maxEtaisyys;
    
    RoboSuunnistin(int maxEtaisyys) { this.maxEtaisyys = maxEtaisyys; }

    public Point2D.Float haeUusiMittauspisteEkaEdition(BTPaketti paketti) {
        Point2D.Float nykySijainti = paketti.getNykySijainti();
        float kulma = (float) Math.toDegrees(paketti.getMittausKulma());
        int[] etaisyydet = paketti.getEtaisyydet();
        int tyhjyyslaskuri = 0;
        int tyhjyysalku = 0;
        int tyhjyysmuisti = 0;
        int tyhjyysalkumuisti = 0;
        
        for (int i = 0; i < etaisyydet.length; i++) {
            if (etaisyydet[i] >= maxEtaisyys) {                // jos ei nähdä mitään
                if (i != 0) {                                  // ja
                    if (etaisyydet[i - 1] < maxEtaisyys) {     // jos edellinen näköviiva näki jotain
                        tyhjyysalku = i;                       // niin tämän tyhjyyden alkukohta on i
                    }
                } else {
                    tyhjyysalku = 0;
                }
                tyhjyyslaskuri++;
            } else {
                if (tyhjyyslaskuri > tyhjyysmuisti) {
                    tyhjyysmuisti = tyhjyyslaskuri;
                    tyhjyysalkumuisti = tyhjyysalku;
                }
                tyhjyyslaskuri = 0;
            }
        }
        
        //Navigoinnin valinta:
        
        Point2D.Float menopiste;
        
        if (tyhjyyslaskuri == etaisyydet.length) { // ei mitään havaittu missään
            
            float sx = (float)(nykySijainti.x + (etaisyydet[18]-150) * Math.sin(Math.toRadians(kulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
            float sy = (float)(nykySijainti.y + (etaisyydet[18]-150) * Math.cos(Math.toRadians(kulma)));
            menopiste = new Point2D.Float(sx,sy);
            //paketti.setUusiSijainti(menopiste);
            //return menopiste;
            
            float kx = (float)(paketti.getNykySijainti().x + etaisyydet[19] * Math.sin(Math.toRadians(kulma)));
            float ky = (float)(paketti.getNykySijainti().y + etaisyydet[19] * Math.cos(Math.toRadians(kulma)));
            Point2D.Float katsepiste = new Point2D.Float(kx,ky);
            paketti.setMittausSuunta(katsepiste);
            
            
        } else if (tyhjyysmuisti == 0) { //kaikki havaitaittu kaikkialla
            if (edettytäyteen) {  
                System.out.println("edettytäyteen=true");
                edettytäyteen = false;
                //käänny(180);
                
                //paketti.setUusiSijainti(paketti.getNykySijainti()); // ei liikettä
                //return nykySijainti;
                menopiste = nykySijainti;
                
                float kx = (float)(nykySijainti.x - maxEtaisyys * Math.sin(Math.toRadians(kulma)));
                float ky = (float)(nykySijainti.y - maxEtaisyys * Math.cos(Math.toRadians(kulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                
            } else {
                System.out.println("edettytäyteen=false");
                //etene(etaisyydet[19] / 2);// edetään suoraan eteenpäin puolet eteenpäin mitatusta pituudesta
                edettytäyteen = true;
                float sx = (float)(nykySijainti.x + (etaisyydet[19] / 2) * Math.sin(Math.toRadians(kulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
                float sy = (float)(nykySijainti.y + (etaisyydet[19] / 2) * Math.cos(Math.toRadians(kulma)));
                menopiste = new Point2D.Float(sx,sy);
                //paketti.setUusiSijainti(menopiste);
                 
                //return menopiste;
                
                float kx = (float)(nykySijainti.x + (etaisyydet[19]) * Math.sin(Math.toRadians(kulma)));
                float ky = (float)(nykySijainti.y + (etaisyydet[19]) * Math.cos(Math.toRadians(kulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                
                
            }
        } else {    //normiliikettä eli hidasta batistiinikävelyä
            //käänny(((tyhjyysalkumuisti * 5) - 90) + ((tyhjyysmuisti / 2) * 5));
            
            //kuinka paljon nyt tahdotaan kääntyä?

            float modkulma = kulma + ((tyhjyysalkumuisti * 5) - 90) + (((float)tyhjyysmuisti / 2) * 5);
            
            
            if ((tyhjyysalkumuisti - 1) >= 0) {
                System.out.println("tam-1>0");
                
                //etene((mtaulu[tyhjyysalkumuisti - 1] + mtaulu[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
                float sx = (float)(nykySijainti.x + ((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.sin(Math.toRadians(modkulma))); //Jos ei toimijnejne...
                float sy = (float)(nykySijainti.y + ((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.cos(Math.toRadians(modkulma)));
                
                float liikuttavana = (float)Math.sqrt(Math.pow(( sx-nykySijainti.x ),2)+Math.pow(( sy-nykySijainti.y ),2)); //lasketaan liikuttava etäisyys pythagoraan lauseella
                
                int lolkulma = (int)((((tyhjyysalkumuisti * 5)) + (((float)tyhjyysmuisti / 2) * 5))/5);
                
                if (liikuttavana > ((etaisyydet[lolkulma]+etaisyydet[lolkulma+1])/2)){ // jos liikuttavana on enemmän kuin mitä etäisyydet taulukossa mainitaan seinäksi:
                    System.out.println("no vittu lol");
                    menopiste = haeUusiSattumanvarainenMittauspiste(paketti);
                    return menopiste;
                    
                }
                
                
                
                menopiste = new Point2D.Float(sx,sy);
                //paketti.setUusiSijainti(menopiste);
                //return menopiste;
                
                
                float kx = (float)(paketti.getNykySijainti().x + (((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.sin(Math.toRadians(modkulma)));
                float ky = (float)(paketti.getNykySijainti().y + (((etaisyydet[tyhjyysalkumuisti - 1] + etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                
            } else {
                System.out.println("joku else");
                //etene((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2);
                float sx = (float)(nykySijainti.x + ((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.sin(Math.toRadians(modkulma))); //Jos ei toimi, koita vaihtaa sini cosinix ja toisinpäin
                float sy = (float)(nykySijainti.y + ((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2) * Math.cos(Math.toRadians(modkulma)));
                menopiste = new Point2D.Float(sx,sy);
                //paketti.setUusiSijainti(menopiste);
                
                //return menopiste;

                float kx = (float)(paketti.getNykySijainti().x + (((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.sin(Math.toRadians(modkulma)));
                float ky = (float)(paketti.getNykySijainti().y + (((etaisyydet[tyhjyysalkumuisti + tyhjyysmuisti]) / 2)*1.5) * Math.cos(Math.toRadians(modkulma)));
                Point2D.Float katsepiste = new Point2D.Float(kx,ky);
                paketti.setMittausSuunta(katsepiste);
                
            }
        }
        
        
        // QUICK & DIRTY + toimii miten sattuu + edelleen seinien läpi
        if (menopiste.x < 0){
            //menopiste.x = 50;
            menopiste = haeUusiSattumanvarainenMittauspiste(paketti);
        }
        if (menopiste.x > 1350){
            //menopiste.x = 1300;
            menopiste = haeUusiSattumanvarainenMittauspiste(paketti);
        }
        if (menopiste.y < 0){
            //menopiste.y = 50;
            menopiste = haeUusiSattumanvarainenMittauspiste(paketti);
        }
        if (menopiste.y > 2000){
            //menopiste.y = 1950;
            menopiste = haeUusiSattumanvarainenMittauspiste(paketti);
        }
        /*
        if (vanhapiste.x == menopiste.x && vanhapiste.y == menopiste.y){
            i++;
        }
        if (vanhapiste.x == menopiste.x && vanhapiste.y == menopiste.y && i == 2){
            i=0;
            menopiste = haeUusiSattumanvarainenMittauspiste(paketti);
        }
        */
        vanhapiste = menopiste;
        return menopiste;
        
    }
    
    //aivokääpiörobotin haeuusimittauspiste:
    //  -huomatkaa että tämä ei toimi kunnolla ainakaan tässä muodossa (robot kiertää kehää)
    //  -tää menee (sisä)seinien läpi että virhe on erittäin todennäkösesti jossain muualla kun uuden mittauspisteen navigoinneissa
    //      menisi myös rectanglen rajojen ulkopuolelle ilman loppupuolen iffejä
    //      saattaa olla että GUI piirtää seinät väärään paikkaan?
    
    //update: nyt vältellään seiniä vähäsen, robot ei varsinaisesti kierrä enää kehää, 
    //  kuva on kaunis, mutta välillä tulee jotain outoja koukeroita kartan ulkopuolelle
    public Point2D.Float haeUusiMittauspisteIdiotEdition(BTPaketti paketti){
        
        int[] etaisyydet = paketti.getEtaisyydet();
        Point2D.Float nykySijainti = paketti.getNykySijainti();
        
        int liikeviiva = -1;
        
        int liikepituus = 600;
        
        Point2D.Float menopiste;
        
        //valitaan pitkin nähdyistä pääilmansuunnista:
        if (etaisyydet[0] >= etaisyydet[18] && etaisyydet[0] >= etaisyydet[36]){
            liikeviiva = 0;
        } else if (etaisyydet[18] >= etaisyydet[0] && etaisyydet[18] >= etaisyydet[36]){
            liikeviiva = 18;
        } else if (etaisyydet[36] >= etaisyydet[18] && etaisyydet[36] >= etaisyydet[0]){
            liikeviiva = 36;
        }
        
        //vähän vaihtelua elämään:
        if ((liikeviiva == 0 || liikeviiva == 36) && (etaisyydet[0] == etaisyydet[36])){
            
            if (Math.random()<0.5D){
                liikeviiva = 0;
            } else liikeviiva = 36;
            
        }
        
        if (liikeviiva == -1){
            System.out.println("eisssss...");//maailmassa on virhe jos tänne mennään.
        }
        
        //kuinka paljon liikutaan:
        if (etaisyydet[liikeviiva] > 500){
            liikepituus = etaisyydet[liikeviiva]-(200+(int)(Math.random()*100));
        } else if (etaisyydet[liikeviiva] > 250){
            liikepituus = etaisyydet[liikeviiva]-(100+(int)(Math.random()*100));
        }
        
        if (etaisyydet[liikeviiva] <= 250){
            //liikepituus = etaisyydet[liikeviiva]-300;   //pakitetaan eli valmiina tunaroimaan
            
            //eipäs pakitetakkaan -> käännytään ympäri:
            
            menopiste = nykySijainti;
            
            if (suunta == 0){
                suunta = 180;
                paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x),(nykySijainti.y-maxEtaisyys)));
            } else if (suunta == 90){
                suunta = 270;
                paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x-maxEtaisyys),(nykySijainti.y)));
            } else if (suunta == 180){
                suunta = 0;
                paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x),(nykySijainti.y+maxEtaisyys)));
            } else if (suunta == 270){
                suunta = 90;
                paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x+maxEtaisyys),(nykySijainti.y)));
            }
        }
        //tämä hajoaa jos alkusuunta ei ole 0 !!!!!!! elikkä +y akselin suuntaan // hajoaa näemmä ihan muutenkin
        else{    //trolololo tweakkasin vähän
            if (suunta == 0){
                if (liikeviiva == 0){
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x - maxEtaisyys),nykySijainti.y));
                    suunta = 270;
                    menopiste = new Point2D.Float((nykySijainti.x - liikepituus),nykySijainti.y);
                } else if (liikeviiva == 18){
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x),nykySijainti.y + maxEtaisyys));
                    suunta = 0;
                    menopiste = new Point2D.Float(nykySijainti.x,(nykySijainti.y+liikepituus));
                } else { //if (liikeviiva == 36)
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x + maxEtaisyys),nykySijainti.y));
                    suunta = 90;
                    menopiste = new Point2D.Float((nykySijainti.x + liikepituus),nykySijainti.y);
                }
            } else if (suunta == 90){
                if (liikeviiva == 0){
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x),(nykySijainti.y+maxEtaisyys)));
                    suunta = 0;
                    menopiste = new Point2D.Float((nykySijainti.x),nykySijainti.y+liikepituus);
                } else if (liikeviiva == 18){
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x+maxEtaisyys),nykySijainti.y));
                    suunta = 90;
                    menopiste = new Point2D.Float(nykySijainti.x+liikepituus,(nykySijainti.y));
                } else { //if (liikeviiva == 36)
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x),nykySijainti.y-maxEtaisyys));
                    suunta = 180;
                    menopiste = new Point2D.Float((nykySijainti.x),nykySijainti.y-liikepituus);
                }
            } else if (suunta == 180){
                if (liikeviiva == 0){
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x + maxEtaisyys),nykySijainti.y));
                    suunta = 90;
                    menopiste = new Point2D.Float((nykySijainti.x + liikepituus),nykySijainti.y);
                } else if (liikeviiva == 18){
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x),nykySijainti.y - maxEtaisyys));
                    suunta = 180;
                    menopiste = new Point2D.Float(nykySijainti.x,(nykySijainti.y-liikepituus));
                } else { //if (liikeviiva == 36)
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x - maxEtaisyys),nykySijainti.y));
                    suunta = 270;
                    menopiste = new Point2D.Float((nykySijainti.x - liikepituus),nykySijainti.y);
                }
            } else {// if (suunta == 270)
                if (liikeviiva == 0){
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x),(nykySijainti.y-maxEtaisyys)));
                    suunta = 180;
                    menopiste = new Point2D.Float((nykySijainti.x),nykySijainti.y-liikepituus);
                } else if (liikeviiva == 18){
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x-maxEtaisyys),nykySijainti.y));
                    suunta = 270;
                    menopiste = new Point2D.Float(nykySijainti.x-liikepituus,(nykySijainti.y));
                } else { //if (liikeviiva == 36)
                    paketti.setMittausSuunta(new Point2D.Float((nykySijainti.x),nykySijainti.y+maxEtaisyys));
                    suunta = 0;
                    menopiste = new Point2D.Float((nykySijainti.x),nykySijainti.y+liikepituus);
                }
            }
        }
        
        if (menopiste != null){
            
            if (menopiste.x > 1350) // ei pitäis mennä ulos mutta tahtoo kuitenkin
                menopiste.x = 1330;
            
            if (menopiste.x < 0)
                menopiste.x = 20;
            
            if (menopiste.y > 2000)
                menopiste.y = 1980;
            
            if (menopiste.y < 0)
                menopiste.y = 20;
            
            
            return menopiste;
        } else {
            menopiste = new Point2D.Float(-666,-666);
        } 
        return menopiste;//error
        
    }

    public Point2D.Float haeUusiSattumanvarainenMittauspiste(BTPaketti paketti) {
        final int[] etaisyydet = paketti.getEtaisyydet();
        int summa = 0;
        int index = -1;
        
        for (int i = 0; i < etaisyydet.length; ++i) {
            if (etaisyydet[i] < 150)
                etaisyydet[i] = 0;
            summa += 1 + etaisyydet[i] * etaisyydet[i];
        }

        if (summa == etaisyydet.length) // Jos kaikki näköviivat < 15 cm...
            return new Point2D.Float(2*paketti.getNykySijainti().x -
                paketti.getMittausSuunta().x, 2*paketti.getNykySijainti().y - 
                paketti.getMittausSuunta().y);

        // Valitse näköviivoista sattumnvaraisesti jokin niin, että pidemmät
        // näköviivat saavat suuremman painoarvon kuin lyhyet.
        summa = (int) Math.floor(Math.random() * summa);
        
        // Etsi valittu näköviiva listasta.
        for (int i = 0; i < etaisyydet.length; ++i)
            if (1 + etaisyydet[i] * etaisyydet[i] <= summa)
                summa -= 1 + etaisyydet[i] * etaisyydet[i];
            else {
                index = i;
                break;
            }

        if (index < 0) // Ehto ei ole koskaan tosi!
            throw new RuntimeException("Indeksi sai laittoman arvon.");

        // Valitse pisin näköviiva.
        JsimRoboNakyma nakyma = new JsimRoboNakyma(paketti.getNykySijainti(),
            (float)Math.toDegrees(paketti.getMittausKulma()),
            etaisyydet.length, etaisyydet[index]);
        Line2D.Float jana = nakyma.getNakotaulu()[index];

        // Liiku näköviivaa pitkin. Älä kuitenkaan mene aivan loppuun asti,
        // jotta ei päädyttäisi jonkin seinän sisään.
        jana.x2 -= (jana.x2 - jana.x1) * 0.85f;
        jana.y2 -= (jana.y2 - jana.y1) * 0.85f;

        // Kun robotti pääty aivan seinän viereen on sillä luontainen taipumus		
        // Ampaista siitä läpi! Siispä varmuuden vuoksi puukotamme tähän		
        // kivat pikku rajatapaustestit.
        // NÄINTÄ EI SITTEN LIVETILANTEESSA VOI KÄYTTÄÄ, koska robotin koordinaatisto
        // on ihan laillisesti origon ympärillä joka suuntaan.
//        boolean puukkoHeiluu = false;
//        if (jana.x2 < 0) { jana.x2 = 0.1f; puukkoHeiluu = true; }
//        if (jana.x2 > 1350) { jana.x2 = 1349.9f; puukkoHeiluu = true; }
//        if (jana.y2 < 0) { jana.y2 = 0.1f; puukkoHeiluu = true; }
//        if (jana.y2 > 2000) { jana.y2 = 1999.9f; puukkoHeiluu = true; }
//        if (puukkoHeiluu) throw new RuntimeException("Robotti haluaa mennä laitojen yli.");

        return new Point2D.Float(jana.x2, jana.y2);
    }
}
