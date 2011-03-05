/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.discovery.listeners;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;

/**
 *
 * @author tim
 */
public class KioskListener extends AbstractServiceListener {
    public static String ServiceType = "_mirakiosk._tcp.local.";
    public KioskListener(JmDNS iJMDNS) {
        super(iJMDNS);
    }

    public void serviceAddedAction(ServiceEvent event) {
        getLogger().info("Kiosk addition: " + event.getName() + "." + event.getType());
    }

    public void serviceRemovedAction(ServiceEvent event) {
        getLogger().info("Kiosk removal: " + event.getName() + "." + event.getType());
    }

    public void serviceResolvedAction(ServiceEvent event) {
        getLogger().info("Kiosk resolved: " + event.getInfo());
    }
}
