/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tim
 */
public class Topology {
    //
    // Member data
    //

    private Map<String, Server> mServers;
    private Map<String, Kiosk> mKiosks;
    private List<TopologyListener> mListeners;


    //
    // Static functionality
    //

    private static Topology mInstance;

    public static Topology getInstance() {
        if (mInstance == null)
            mInstance = new Topology();
        return mInstance;
    }


    //
    // Construction and destruction
    //

    private Topology() {
        mServers = new HashMap<String, Server>();
        mKiosks = new HashMap<String, Kiosk>();
        mListeners = new ArrayList<TopologyListener>();
    }


    //
    // Getters and setters
    //

    public synchronized void addListener(TopologyListener iListener) {
        mListeners.add(iListener);
    }

    public synchronized Collection<Kiosk> getKiosks() {
        List tKiosks = new ArrayList<Kiosk>();
        for (Kiosk tKiosk : mKiosks.values())
            tKiosks.add(new Kiosk(tKiosk));
        return tKiosks;
    }

    public synchronized Kiosk getKiosk(String iName) {
        return new Kiosk(mKiosks.get(iName));
    }

    public synchronized void addKiosk(Kiosk iKiosk) throws TopologyException {
        if (mKiosks.containsKey(iKiosk.getName()))
            throw new TopologyException("Topology already contains kiosk with name " + iKiosk.getName());

        mKiosks.put(iKiosk.getName(), iKiosk);

        kioskAdded(iKiosk);
    }

    public synchronized void updateKiosk(Kiosk iKioskNew) throws TopologyException {
        if (! mKiosks.containsKey(iKioskNew.getName()))
            throw new TopologyException("Topology does not yet contain a kiosk with name " + iKioskNew.getName());

        Kiosk tKioskOld = mKiosks.remove(iKioskNew.getName());
        mKiosks.put(iKioskNew.getName(), iKioskNew);

        kioskUpdated(tKioskOld, iKioskNew);
    }

    public synchronized Collection<Server> getServers() {
        List tServers = new ArrayList<Server>();
        for (Server tServer : mServers.values())
            tServers.add(new Server(tServer));
        return tServers;
    }

    public synchronized Server getServer(String iName) {
        return mServers.get(iName);
    }

    public synchronized void addServer(Server iServer) throws TopologyException {
        if (mServers.containsKey(iServer.getName()))
            throw new TopologyException("Topology already contains server with name " + iServer.getName());
        mServers.put(iServer.getName(), iServer);

        serverAdded(iServer);
    }

    public synchronized void updateServer(Server iServerNew) throws TopologyException {
        if (! mServers.containsKey(iServerNew.getName()))
            throw new TopologyException("Topology does not yet contain a server with name " + iServerNew.getName());

        Server tServerOld = mServers.remove(iServerNew.getName());
        mServers.put(iServerNew.getName(), iServerNew);

        serverUpdated(tServerOld, iServerNew);
    }

    public synchronized Collection<Machine> getMachines() {
        Collection<Machine> oMachines = new ArrayList<Machine>();
        oMachines.addAll(getServers());
        oMachines.addAll(getKiosks());
        return oMachines;
    }


    //
    // Events
    //

    private void kioskAdded(Kiosk iKiosk) {
        for (TopologyListener tListener : mListeners)
            tListener.kioskAddedAction(iKiosk);
    }

    private void kioskUpdated(Kiosk iKioskOld, Kiosk iKioskNew) {
        for (TopologyListener tListener : mListeners)
            tListener.kioskUpdatedAction(iKioskOld, iKioskNew);
    }

    private void serverAdded(Server iServer) {
        for (TopologyListener tListener : mListeners)
            tListener.serverAddedAction(iServer);
    }

    private void serverUpdated(Server iServerOld, Server iServerNew) {
        for (TopologyListener tListener : mListeners)
            tListener.serverUpdatedAction(iServerOld, iServerNew);
    }
}
