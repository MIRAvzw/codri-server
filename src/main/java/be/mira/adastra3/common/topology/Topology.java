/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
    }


    //
    // Getters and setters
    //

    public synchronized Collection<Kiosk> getKiosks() {
        return mKiosks.values();
    }

    public synchronized Kiosk getKiosk(String iName) {
        return mKiosks.get(iName);
    }

    public synchronized void addKiosk(Kiosk iKiosk) throws TopologyException {
        if (mKiosks.containsKey(iKiosk.getName()))
            throw new TopologyException("Topology already contains kiosk with name " + iKiosk.getName());
        mKiosks.put(iKiosk.getName(), iKiosk);
    }

    public synchronized Collection<Server> getServers() {
        return mServers.values();
    }

    public synchronized Server getServer(String iName) {
        return mServers.get(iName);
    }

    public synchronized void addServer(Server iServer) throws TopologyException {
        if (mServers.containsKey(iServer.getName()))
            throw new TopologyException("Topology already contains server with name " + iServer.getName());
        mServers.put(iServer.getName(), iServer);
    }

    public synchronized Collection<Machine> getMachines() {
        Collection<Machine> oMachines = new ArrayList<Machine>();
        oMachines.addAll(getServers());
        oMachines.addAll(getKiosks());
        return oMachines;
    }
}
