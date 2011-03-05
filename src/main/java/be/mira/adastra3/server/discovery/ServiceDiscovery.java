/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.discovery;

import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.discovery.listeners.KioskListener;
import be.mira.adastra3.server.discovery.listeners.ServerListener;
import be.mira.adastra3.server.discovery.listeners.TypeListener;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import java.io.IOException;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

/**
 *
 * @author tim
 */
public class ServiceDiscovery extends Service {
    private JmDNS mJMDNS;
    private ServiceInfo mService;

    public ServiceDiscovery() throws ServiceSetupException {
        super();
        getLogger().debug("Configuring subsystem");

        // Server port
        Integer iPort;
        try {
            iPort = Integer.parseInt(getProperty("port", "8080"));
            if (iPort <= 0 || iPort > 65536)
                throw new ServiceSetupException("Server port out of valid range");
        }
        catch (NumberFormatException e) {
            throw new ServiceSetupException("Non-integer port specification");
        }

        // JmDNS object
        try {
            mJMDNS = JmDNS.create();
        }
        catch (IOException e) {
            throw new ServiceSetupException(e);
        }

        // Service
        mService = ServiceInfo.create(
                getProperty("type", "_miraserver._tcp.local."),
                getProperty("name", "MIRA Ad-Astra III server"),
                iPort,
                getProperty("description", "The application server for the MIRA Ad-Astra III application."));
    }

    public void run() throws ServiceRunException {
        getLogger().debug("Starting subsystem");

        try {
            mJMDNS.registerService(mService);
            
            mJMDNS.addServiceListener(KioskListener.ServiceType, new KioskListener());
            mJMDNS.addServiceListener(ServerListener.ServiceType, new ServerListener());
            mJMDNS.addServiceTypeListener(new TypeListener());
        }
        catch (IOException e) {
            throw new ServiceRunException(e);
        }
    }

    public void stop() throws ServiceRunException {
        getLogger().debug("Stopping subsystem");

        try {
            mJMDNS.close();
        }
        catch (IOException e) {
            throw new ServiceRunException(e);
        }
    }

}
