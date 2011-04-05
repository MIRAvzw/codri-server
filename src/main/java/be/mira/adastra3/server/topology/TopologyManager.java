/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.topology;

import be.mira.adastra3.common.topology.Kiosk;
import be.mira.adastra3.common.topology.Server;
import be.mira.adastra3.common.topology.TopologyListener;
import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;

/**
 *
 * @author tim
 */
public class TopologyManager extends Service implements TopologyListener {
    //
    // Data members
    //


    //
    // Construction and destruction
    //

    public TopologyManager() throws ServiceSetupException {
    }


    //
    // Service interface
    //

    @Override
    public void run() throws ServiceRunException {
    }

    @Override
    public void stop() throws ServiceRunException {
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
