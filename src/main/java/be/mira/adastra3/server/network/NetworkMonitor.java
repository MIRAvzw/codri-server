/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.network;

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
public class NetworkMonitor extends Service {
    //
    // Data members
    //
    private JmDNS mJMDNS;
    private ServiceInfo mService;


    //
    // Construction and destruction
    //

    public NetworkMonitor() throws ServiceSetupException {
        super();

        // Server port
        Integer iPort;
        try {
            iPort = Integer.parseInt(getProperty("port", "8080"));
            if (iPort <= 0 || iPort > 65536)
                throw new ServiceSetupException("Server port out of valid range");
            getLogger().debug("Using port " + iPort);
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


    //
    // Service interface
    //

    public void run() throws ServiceRunException {
        try {
            mJMDNS.registerService(mService);
            
            mJMDNS.addServiceListener(KioskListener.ServiceType, new KioskListener(mJMDNS));
            mJMDNS.addServiceListener(ServerListener.ServiceType, new ServerListener(mJMDNS));
            mJMDNS.addServiceTypeListener(new TypeListener());
        }
        catch (IOException e) {
            throw new ServiceRunException(e);
        }
    }

    public void stop() throws ServiceRunException {
        try {
            mJMDNS.unregisterAllServices();
            mJMDNS.close();
        }
        catch (IOException e) {
            throw new ServiceRunException(e);
        }
    }

}
