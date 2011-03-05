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
public class ServerListener extends AbstractServiceListener {
    public static String ServiceType = "_miraserver._tcp.local.";

    public ServerListener(JmDNS iJMDNS) {
        super(iJMDNS);
    }

    public void serviceAddedAction(ServiceEvent event) {
        getLogger().info("Server addition: " + event.getName() + "." + event.getType());
    }

    public void serviceRemovedAction(ServiceEvent event) {
        getLogger().info("Server removal: " + event.getName() + "." + event.getType());
    }

    public void serviceResolvedAction(ServiceEvent event) {
        getLogger().info("Server resolved: " + event.getInfo());
    }
}
