package slam;

public class Luokkakaavio {
/*
RoboOhjain on rajapinta robotille päin.
RoboOhjaimelle annetaan luonnin yhteydessä BT-rajapinnan toteuttava luokka.
Jos kyseessä on simulointi, niin JSimBTYhteys luo itselleen JSimRobon.

Yhteenveto: Jos siis missään haluat päästä robottiin käsiksi, käytä
RoboOhjainta.

    +-- RoboOhjain
    |
    +-+ BTYhteys <interface>
      |
      +-+ JSimBTYhteys
      | |
      | +- JSimRobo1
      |
      + NXTBTYhteys
*/
}
