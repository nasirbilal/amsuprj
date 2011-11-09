/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slam;

/**
 *
 * @author Mudi
 */
public class BTYhteys {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        NXTConnector conn = new NXTConnector();

        conn.addLogListener(new NXTCommLogListener() {

            public void logEvent(String message) {
                System.out.println("RobottiOrja Log.listener: " + message);

            }

            public void logEvent(Throwable throwable) {
                System.out.println("RobottiOrja Log.listener - stack trace: ");
                throwable.printStackTrace();
            }
        });
        // Luodaan yhteys Jantuseen
        boolean connected = conn.connectTo("Jantunen");

        if (!connected) {
            System.err.println("Yhdistaminen epaonnistui");
            System.exit(-1);
        }

        DataOutputStream dos = conn.getDataOut();
        DataInputStream dis = conn.getDataIn();
        int input;
        System.out.println("Testataan ison joukon lähettämistä ja mitataan aika");
        long startingTime = System.nanoTime();
        for (int i = 0; i < 16; i++) {
            try {
                dos.writeInt(i);
                dos.flush();

            } catch (IOException ioe) {
                //System.out.println("IO Exception kirjoittaessa:");
                System.out.println(ioe.getMessage());
                break;
            }

            try {
                input = dis.readInt();
            } catch (IOException ioe) {
                //System.out.println("IO Exception lukiessa:");
                System.out.println(ioe.getMessage());
                break;
            }
        }

        long endingTime = System.nanoTime();
        System.out.println("Lähetettiin 100000 lukua ajassa " + (endingTime - startingTime) + " millisekunttia");


        System.out.println("################################################################");

        try {
            dis.close();
            dos.close();
            conn.close();
        } catch (IOException ioe) {
            System.out.println("IOException sulkiessa yhteytta:");
            System.out.println(ioe.getMessage());
        }
    }
}