/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.discovery.listeners;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceTypeListener;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class TypeListener implements ServiceTypeListener {
    Logger mLogger = Logger.getLogger(TypeListener.class);

    @Override
    public void serviceTypeAdded(ServiceEvent event) {
        mLogger.debug("Discovered new service type: " + event.getType());
    }
    
    @Override
    public void subTypeForServiceTypeAdded(ServiceEvent event) {
        mLogger.debug("Discovered new service subtype: " + event.getType());
    }
}
