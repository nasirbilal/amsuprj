package slam;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

/**
 *
 * @author L
 */
public interface BTYhteys {

    private JsimRobo robo;

    public Mittaustulokset haeMittaustulokset();
    public boolean Laheta(BTPaketti paketti);
}
