/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.discovery.listeners;

import be.mira.adastra3.common.topology.Topology;
import be.mira.adastra3.common.topology.Server;
import be.mira.adastra3.common.topology.Machine.State;
import be.mira.adastra3.common.topology.TopologyException;
import java.net.InetAddress;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;

/**
 *
 * @author tim
 */
public class ServerListener extends AbstractServiceListener {
    public static String ServiceType = "_miraserver._tcp.local.";
    public ServerListener(JmDNS iJMDNS) {
        super(iJMDNS);
    }

    public void serviceAddedAction(ServiceEvent iServiceEvent) {
        getLogger().info("Server addition: " + iServiceEvent.getName() + "." + iServiceEvent.getType());

        // Alter the topology
        Server tServer = new Server(iServiceEvent.getName());
        try {
            Topology.getInstance().addServer(tServer);
        } catch (TopologyException e) {
            getLogger().warn("Server " + iServiceEvent.getName() + " seems to be present already", e);
        }
    }

    public void serviceRemovedAction(ServiceEvent iServiceEvent) {
        getLogger().info("Server removal: " + iServiceEvent.getName() + "." + iServiceEvent.getType());

        // Alter the topology
        Server tServer = Topology.getInstance().getServer(iServiceEvent.getName());
        if (tServer != null) {
            tServer.setState(State.OFFLINE);
            tServer.clearInetAddresses();
        } else
            getLogger().warn("Could not remove server " + iServiceEvent.getName() + ", not present in topology");
    }

    public void serviceResolvedAction(ServiceEvent iServiceEvent) {
        getLogger().info("Server resolved: " + iServiceEvent.getInfo());

        // Alter the topology
        Server tServer = Topology.getInstance().getServer(iServiceEvent.getName());
        if (tServer != null) {
            tServer.setState(State.ONLINE);
            ServiceInfo iServiceInfo = iServiceEvent.getInfo();
            for (InetAddress tInetAddress : iServiceInfo.getInetAddresses()) {
                tServer.addInetAddress(tInetAddress);
            }
        } else
            getLogger().warn("Could not resolve server " + iServiceEvent.getName() + ", not present in topology");
    }
}
