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

    public void addListener(TopologyListener iListener) {
        synchronized(this) {
            mListeners.add(iListener);
        }
    }

    public void removeListener(TopologyListener iListener) {
        synchronized(this) {
            mListeners.remove(iListener);
        }
    }

    public Collection<Kiosk> getKiosks() {
        synchronized(this) {
            List tKiosks = new ArrayList<Kiosk>();
            for (Kiosk tKiosk : mKiosks.values())
                tKiosks.add(new Kiosk(tKiosk));
            return tKiosks;
        }
    }

    public Kiosk getKiosk(String iName) {
        synchronized(this) {
                if (mKiosks.containsKey(iName))
                    return new Kiosk(mKiosks.get(iName));
                else
                    return null;
        }
    }

    public void addKiosk(Kiosk iKiosk) throws TopologyException {
        synchronized(this) {
            if (mKiosks.containsKey(iKiosk.getName()))
                throw new TopologyException("Topology already contains kiosk with name " + iKiosk.getName());

            mKiosks.put(iKiosk.getName(), iKiosk);
        }

        kioskAdded(iKiosk);
    }

    public void updateKiosk(Kiosk iKioskNew) throws TopologyException {
        Kiosk tKioskOld;
        synchronized(this) {
            if (! mKiosks.containsKey(iKioskNew.getName()))
                throw new TopologyException("Topology does not yet contain a kiosk with name " + iKioskNew.getName());

            tKioskOld = mKiosks.remove(iKioskNew.getName());
            mKiosks.put(iKioskNew.getName(), iKioskNew);
        }

        kioskUpdated(tKioskOld, iKioskNew);
    }

    public Collection<Server> getServers() {
        synchronized(this) {
            List tServers = new ArrayList<Server>();
            for (Server tServer : mServers.values())
                tServers.add(new Server(tServer));
            return tServers;
        }
    }

    public Server getServer(String iName) {
        synchronized(this) {
            if (mServers.containsKey(iName))
                return new Server(mServers.get(iName));
            else
                return null;
        }
    }

    public void addServer(Server iServer) throws TopologyException {
        synchronized(this) {
            if (mServers.containsKey(iServer.getName()))
                throw new TopologyException("Topology already contains server with name " + iServer.getName());
            mServers.put(iServer.getName(), iServer);
        }

        serverAdded(iServer);
    }

    public void updateServer(Server iServerNew) throws TopologyException {
        Server tServerOld;
        synchronized(this) {
            if (! mServers.containsKey(iServerNew.getName()))
                throw new TopologyException("Topology does not yet contain a server with name " + iServerNew.getName());

            tServerOld = mServers.remove(iServerNew.getName());
            mServers.put(iServerNew.getName(), iServerNew);
        }

        serverUpdated(tServerOld, iServerNew);
    }

    public Collection<Machine> getMachines() {
        synchronized(this) {
            Collection<Machine> oMachines = new ArrayList<Machine>();
            oMachines.addAll(getServers());
            oMachines.addAll(getKiosks());
            return oMachines;
        }
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
    
    
    //
    // Auxiliary
    //
    
    // TODO: use
    private void checkMachine(Machine iMachine) throws TopologyException {
        if (iMachine.getState() == Machine.State.ONLINE && iMachine.getInetAddresses().size() == 0)
            throw new TopologyException("Online machine without address");
        if (iMachine.getPort() <= 0 || iMachine.getPort() > 65535)
            throw new TopologyException("Invalid port specification");
    }
}
