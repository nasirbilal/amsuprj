package slam;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author juho vainio
 */
public class JsimRoboNakyma {

    private Line2D.Float sateet[]; /// Robotin "katsetta" kuvaavat säteet.

    /**
     * @param paikka Robotin sijainti mittaushetkellä.
     * @param suunta Robotin katseen suunta asteina yksikköympyrän mukaisesti.
     * @param mittausmaara Mittausten määrä (default 37 eli 5 asteen välein).
     * @param infraEtaisyys Robotin "katseen" (IR-sensorin) kantama MILLIMETREINÄ.
     */
    public JsimRoboNakyma(Point2D.Float paikka, float katsesuunta,
                          int mittausmaara, int infraEtaisyys) {

        // Luodaan robotin näkyöviivoista robon suuntaan ja paikkaan
        // perustuen taulukko, jota vertaillaan sitten kartan viivoihin.
        sateet = new Line2D.Float[mittausmaara]; // Näköviivojen taulukko

        if (mittausmaara <= 1) {
            // Katsekulma on suoraan eteenpäin.
            sateet[0] = new Line2D.Float(paikka, new Point2D.Float(
                    paikka.x + infraEtaisyys, paikka.y));
            return;
        }

        // Kuinka suuri kulma jää katseviivojen väliin.
        float katsekulma = 180.0f / (mittausmaara - 1.0f);
        katsesuunta = katsesuunta + 90; // Aloita vasemmalta: muista yksikköympyrä!!!

        for (int i = 0; i < sateet.length; i++) {
            double kulma = Math.toRadians(katsesuunta);
            double x = (paikka.x + infraEtaisyys * Math.cos(kulma));
            double y = (paikka.y + infraEtaisyys * Math.sin(kulma));

            // Näköviiva menee robotin nykyisestä paikasta laskettuun pisteeseen.
            sateet[i] = new Line2D.Float(paikka, new Point2D.Float((float)x, (float)y));
            katsesuunta -= katsekulma; // Seuraavan katseen suunta: muista yksikköympyrä!!!
        }
    }

    public Line2D.Float[] getNakotaulu() {
        return sateet;
    }

    /**
     * Laske janojen leikkauspiste.
     * 
     * @param s Robotin katsomista kuvaava jana.
     * @param t Ympäristössä olevaa estettä eli seinää kuvaava jana.
     * @return Janojen leikkauspisteen tai null, jos ne eivät leikkaa.
     */
    public Point2D.Float laskeLeikkauspiste(Line2D.Float s, Line2D.Float t) {
        float x, y, a1, a2, b1, b2;

        if (!s.intersectsLine(t))
            return null;
        
        // Find the point of intersection of the lines extended to infinity
        if (s.x1 == s.x2 && t.y1 == t.y2) { // perpendicular
            x = s.x1;
            y = t.y1;
        } else if (s.y1 == s.y2 && t.x1 == t.x2) { // perpendicular
            x = t.x1;
            y = s.y1;
        } else if (s.y2 == s.y1 || t.y2 == t.y1) { // one line is horizontal
            a1 = (s.y2 - s.y1) / (s.x2 - s.x1);
            b1 = s.y1 - a1 * s.x1;
            a2 = (t.y2 - t.y1) / (t.x2 - t.x1);
            b2 = t.y1 - a2 * t.x1;

            if (a1 == a2)
                return null; // parallel

            x = (b2 - b1) / (a1 - a2);
            y = a1 * x + b1;
        } else {
            a1 = (s.x2 - s.x1) / (s.y2 - s.y1);
            b1 = s.x1 - a1 * s.y1;
            a2 = (t.x2 - t.x1) / (t.y2 - t.y1);
            b2 = t.x1 - a2 * t.y1;

            if (a1 == a2)
                return null; // parallel

            y = (b2 - b1) / (a1 - a2);
            x = a1 * y + b1;
        }

        return new Point2D.Float(x, y);
    }
}
