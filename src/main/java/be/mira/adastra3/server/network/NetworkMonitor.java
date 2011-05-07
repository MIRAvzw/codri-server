/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.network;

import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.model.message.header.STAllHeader;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.types.ServiceId;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;

/**
 *
 * @author tim
 */
public class NetworkMonitor extends Service {
    //
    // Data members
    //
    private UpnpService mUpnpService;


    //
    // Construction and destruction
    //

    public NetworkMonitor() throws ServiceSetupException {
        super();
        
        try {
            mUpnpService = new UpnpServiceImpl();

            // Add a listener for device registration events
            mUpnpService.getRegistry().addListener(createRegistryListener(mUpnpService)
            );

        } catch (Exception ex) {
            throw new ServiceSetupException(ex);
        }
    }


    //
    // Service interface
    //

    public void run() throws ServiceRunException {
        try {
            // Broadcast a search message for all devices
            mUpnpService.getControlPoint().search(
                    new STAllHeader()
            );

        } catch (Exception ex) {
            throw new ServiceRunException(ex);
        }
    }

    public void stop() throws ServiceRunException {
        try {
            mUpnpService.shutdown();
        }
        catch (Exception e) {
            throw new ServiceRunException(e);
        }
    }
    
    RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {
            ServiceId tDataServiceId = new UDAServiceId("Data");
            ServiceId tApplicationServiceId = new UDAServiceId("Application");

            @Override
            public void remoteDeviceAdded(Registry iRegistry, RemoteDevice iDevice) {

                RemoteService tService;
                if ((tService = iDevice.findService(tDataServiceId)) != null) {

                    getLogger().debug("Service discovered: " + tService);
                    //executeAction(upnpService, tService);

                }

            }

            @Override
            public void remoteDeviceRemoved(Registry iRegistry, RemoteDevice iDevice) {
                RemoteService tService;
                if ((tService = iDevice.findService(tDataServiceId)) != null) {
                    getLogger().debug("Service disappeared: " + tService);
                }
            }

        };
    }

}
