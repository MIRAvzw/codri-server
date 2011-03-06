/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.discovery.listeners;

import be.mira.adastra3.common.Kiosk;
import be.mira.adastra3.common.Machine.State;
import be.mira.adastra3.common.Topology;
import be.mira.adastra3.common.TopologyException;
import java.net.InetAddress;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;

/**
 *
 * @author tim
 */
public class KioskListener extends AbstractServiceListener {
    public static String ServiceType = "_mirakiosk._tcp.local.";
    public KioskListener(JmDNS iJMDNS) {
        super(iJMDNS);
    }

    public void serviceAddedAction(ServiceEvent iServiceEvent) {
        getLogger().info("Kiosk addition: " + iServiceEvent.getName() + "." + iServiceEvent.getType());

        // Alter the topology
        Kiosk tKiosk = new Kiosk(iServiceEvent.getName());
        try {
            Topology.getInstance().addKiosk(tKiosk);
        } catch (TopologyException e) {
            getLogger().warn("Kiosk " + iServiceEvent.getName() + " seems to be present already", e);
        }
    }

    public void serviceRemovedAction(ServiceEvent iServiceEvent) {
        getLogger().info("Kiosk removal: " + iServiceEvent.getName() + "." + iServiceEvent.getType());

        // Alter the topology
        Kiosk tKiosk = Topology.getInstance().getKiosk(iServiceEvent.getName());
        if (tKiosk != null) {
            tKiosk.setState(State.OFFLINE);
            tKiosk.clearInetAddresses();
        } else
            getLogger().warn("Could not remove kiosk " + iServiceEvent.getName() + ", not present in topology");
    }

    public void serviceResolvedAction(ServiceEvent iServiceEvent) {
        getLogger().info("Kiosk resolved: " + iServiceEvent.getInfo());

        // Alter the topology
        Kiosk tKiosk = Topology.getInstance().getKiosk(iServiceEvent.getName());
        if (tKiosk != null) {
            tKiosk.setState(State.ONLINE);
            ServiceInfo iServiceInfo = iServiceEvent.getInfo();
            for (InetAddress tInetAddress : iServiceInfo.getInetAddresses()) {
                tKiosk.addInetAddress(tInetAddress);
            }
        } else
            getLogger().warn("Could not resolve kiosk " + iServiceEvent.getName() + ", not present in topology");
    }
}
