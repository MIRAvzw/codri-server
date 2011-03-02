/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.mueumkiosk.server.mdns;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

/**
 *
 * @author tim
 */
public class KioskListener implements ServiceListener {
    public static String ServiceType = "_mirakiosk._tcp.local.";

    @Override
    public void serviceAdded(ServiceEvent event) {
        System.out.println("+ Kiosk toegevoegd: " + event.getName() + "." + event.getType());
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        System.out.println("- Kiosk verwijderd: " + event.getName() + "." + event.getType());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        System.out.println("* Kiosk geresolved: " + event.getInfo());
    }
}
