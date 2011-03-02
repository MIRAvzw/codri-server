/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import be.mira.mueumkiosk.server.mdns.KioskListener;
import be.mira.mueumkiosk.server.mdns.ServerListener;
import be.mira.mueumkiosk.server.mdns.TypeListener;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;

/**
 *
 * @author tim
 */
public class Main {

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        try {
            Logger logger = Logger.getLogger(JmDNS.class.getName());
            ConsoleHandler handler = new ConsoleHandler();
            logger.addHandler(handler);
            logger.setLevel(Level.FINER);
            handler.setLevel(Level.FINER);

            System.out.println("* Initialisatie mDNS");
            JmDNS jmdns = JmDNS.create();

            jmdns.addServiceListener(KioskListener.ServiceType, new KioskListener());
            jmdns.addServiceListener(ServerListener.ServiceType, new ServerListener());
            jmdns.addServiceTypeListener(new TypeListener());

            System.out.println("* Luisteren naar berichten");
            System.out.println("  Stuur 'q\\n' om te stoppen");
            int b;
            while ((b = System.in.read()) != -1 && (char) b != 'q') {
                /* Stub */
            }

            System.out.println("* Afluiten mDNS");
            jmdns.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
