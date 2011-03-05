/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.discovery.listeners;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public abstract class AbstractServiceListener implements ServiceListener {
    private Logger mLogger;
    private JmDNS mJMDNS;

    public AbstractServiceListener(JmDNS iJMDNS) {
        mLogger = Logger.getLogger(this.getClass());
        mJMDNS = iJMDNS;
    }

    public Logger getLogger() {
        return mLogger;
    }

    /*
     * Called when a unknown service is detected. As each service specifies
     * the name of the host it runs on, this method will be called the first
     * time the service is detected. When it consequently shuts down and restarts
     * while the server is still running, only a new Resolved event will
     * happen!
     *
     * Practical use: add the service as a new entry to the application state.
     */
    @Override
    public final void serviceAdded(ServiceEvent event) {
        synchronized(this) {
            serviceAddedAction(event);
            final boolean persistent = false;
            mJMDNS.getServiceInfo(event.getInfo().getTypeWithSubtype(), event.getInfo().getName(), persistent);
        }
    }
    public abstract void serviceAddedAction(ServiceEvent event);

    /*
     * Called when a service shuts down. This does not mean JmDNS forgets
     * about the specific service.
     *
     * Practical use: mark the service as off-line.
     */
    @Override
    public final void serviceRemoved(ServiceEvent event) {
        synchronized(this) {
            serviceRemovedAction(event);
        }
    }
    public abstract void serviceRemovedAction(ServiceEvent event);

    /*
     * This is called when a service (either new or already known) is resolved.
     * This means the exact adress of the service is now known.
     *
     * Practical use: mark the service as on-line. It'll be already know, as
     * the Resolved event is guarantueed to be called after Added has been
     * thrown already.
     */
    @Override
    public final void serviceResolved(ServiceEvent event) {
        synchronized(this) {
            serviceResolvedAction(event);
        }
    }
    public abstract void serviceResolvedAction(ServiceEvent event);
}
