/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.discovery.listeners;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class ServerListener implements ServiceListener {
    Logger mLogger = Logger.getLogger(ServiceListener.class);
    public static String ServiceType = "_miraserver._tcp.local.";

    @Override
    public void serviceAdded(ServiceEvent event) {
        mLogger.debug("Server addition: " + event.getName() + "." + event.getType());
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        mLogger.debug("Server removal: " + event.getName() + "." + event.getType());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        mLogger.debug("Server resolved: " + event.getInfo());
    }
}
