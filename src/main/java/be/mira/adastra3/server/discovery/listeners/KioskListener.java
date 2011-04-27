/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.discovery.listeners;

import be.mira.adastra3.common.topology.Topology;
import be.mira.adastra3.common.topology.Kiosk;
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
public class KioskListener extends AbstractServiceListener {
    public static String ServiceType = "_mirakiosk._tcp.local.";
    public KioskListener(JmDNS iJMDNS) {
        super(iJMDNS);
    }

    public void serviceAddedAction(ServiceEvent iServiceEvent) {
        getLogger().debug("Kiosk addition: " + iServiceEvent.getName() + "." + iServiceEvent.getType());

        // Alter the topology
        Kiosk tKiosk = new Kiosk(iServiceEvent.getName());
        try {
            Topology.getInstance().addKiosk(tKiosk);
        } catch (Exception e) {
            getLogger().error("Could not add kiosk " + iServiceEvent.getName(), e);
        }
    }

    public void serviceRemovedAction(ServiceEvent iServiceEvent) {
        getLogger().debug("Kiosk removal: " + iServiceEvent.getName() + "." + iServiceEvent.getType());

        // Alter the topology
        try {
            Kiosk tKiosk = Topology.getInstance().getKiosk(iServiceEvent.getName());
            if (tKiosk != null) {
                tKiosk.setState(State.OFFLINE);
                tKiosk.clearInetAddresses();
                Topology.getInstance().updateKiosk(tKiosk);
            } else
                getLogger().error("Could not remove kiosk " + iServiceEvent.getName() + ", not present in topology");
        } catch (Exception e) {
            getLogger().error("Could not remove kiosk " + iServiceEvent.getName(), e);
        }
    }

    public void serviceResolvedAction(ServiceEvent iServiceEvent) {
        getLogger().debug("Kiosk resolved: " + iServiceEvent.getInfo());

        // Alter the topology
        try {
            Kiosk tKiosk = Topology.getInstance().getKiosk(iServiceEvent.getName());
            if (tKiosk != null) {
                tKiosk.setState(State.ONLINE);
                ServiceInfo iServiceInfo = iServiceEvent.getInfo();
                for (InetAddress tInetAddress : iServiceInfo.getInetAddresses()) {
                    tKiosk.addInetAddress(tInetAddress);
                }
                Topology.getInstance().updateKiosk(tKiosk);
            } else
                getLogger().error("Could not resolve kiosk " + iServiceEvent.getName() + ", not present in topology");
        }
        catch (Exception e) {
            getLogger().error("Could not resolve kiosk " + iServiceEvent.getName(), e);
        }
    }
}
