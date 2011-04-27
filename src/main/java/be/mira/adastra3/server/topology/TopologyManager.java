/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.topology;

import be.mira.adastra3.common.topology.Kiosk;
import be.mira.adastra3.common.topology.Machine;
import be.mira.adastra3.common.topology.ProxyFactory;
import be.mira.adastra3.common.topology.Server;
import be.mira.adastra3.common.topology.Topology;
import be.mira.adastra3.common.topology.TopologyListener;
import be.mira.adastra3.common.topology.proxies.KioskProxy;
import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class TopologyManager extends Service implements TopologyListener {
    //
    // Member data
    //
    
    ProxyFactory mProxyFactory;
    Logger mLogger;


    //
    // Construction and destruction
    //

    public TopologyManager() throws ServiceSetupException {
        mProxyFactory = new ProxyFactory();
        mLogger = Logger.getLogger(this.getClass());
    }


    //
    // Service interface
    //

    @Override
    public void run() throws ServiceRunException {
        Topology.getInstance().addListener(this);
    }

    @Override
    public void stop() throws ServiceRunException {
        Topology.getInstance().removeListener(this);
    }


    //
    // Topology listener
    //

    @Override
    public void kioskAddedAction(Kiosk iKiosk) {
        getLogger().debug("Kiosk added: " + iKiosk.getName());

        // TODO
    }

    @Override
    public void kioskUpdatedAction(Kiosk iKioskOld, Kiosk iKioskNew) {
        getLogger().debug("Kiosk updated: " + iKioskNew.getName());
        
        if (iKioskOld.getState() != Machine.State.ONLINE && iKioskNew.getState() == Machine.State.ONLINE) {
            try {
                KioskProxy tKioskProxy = mProxyFactory.getKioskProxy(iKioskNew);
                tKioskProxy.setInterfaceLocation("foobar");
            } catch (UndeclaredThrowableException e) {
                mLogger.error("Could not configure newly appeared kiosk", e.getCause());
            }
        }

        // TODO
    }

    @Override
    public void serverAddedAction(Server iServer) {
        getLogger().debug("Server added: " + iServer.getName());

        // TODO
    }

    @Override
    public void serverUpdatedAction(Server iServerOld, Server iServerNew) {
        getLogger().debug("Server updated: " + iServerNew.getName());

        // TODO
    }
}
