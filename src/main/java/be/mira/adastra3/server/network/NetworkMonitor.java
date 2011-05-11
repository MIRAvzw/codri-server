/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.network;

import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.network.controls.DeviceControl;
import be.mira.adastra3.server.network.controls.ApplicationControl;
import java.util.UUID;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.model.message.header.STAllHeader;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.types.UDN;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;

/**
 *
 * @author tim
 */
public class NetworkMonitor extends Service {

    //
    // Construction and destruction
    //

    public NetworkMonitor() throws ServiceSetupException {
        super();
        
        try {
            // Add a listener for device registration events
            Network.getInstance().getUpnpService().getRegistry().addListener(
                    createRegistryListener(Network.getInstance().getUpnpService())
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
            getLogger().debug("Looking for devices");
            Network.getInstance().getUpnpService().getControlPoint().search(
                    new STAllHeader()
            );

        } catch (Exception ex) {
            throw new ServiceRunException(ex);
        }
    }

    public void stop() throws ServiceRunException {
        try {
            Network.getInstance().getUpnpService().shutdown();
        }
        catch (Exception e) {
            throw new ServiceRunException(e);
        }
    }
    
    RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {
            // Device addition
            @Override
            public void remoteDeviceAdded(Registry iRegistry, RemoteDevice iDevice) {
                Network tNetwork = Network.getInstance();
                getLogger().debug("New device added to registry: " + iDevice.getDisplayString());
                UUID tUuid;
                try {
                    tUuid = _convertUdn(iDevice.getIdentity().getUdn());
                } catch (NetworkException iException) {
                    tNetwork.emitError("Cannot proceed without having the device identifier", iException);
                    return;
                }

                // Scan for services
                RemoteService tService;
                if ((tService = iDevice.findService(DeviceControl.ServiceId)) != null) {
                    getLogger().debug("Registering kiosk service");
                    try {
                        DeviceControl tKioskControl = new DeviceControl(tService);
                        Network.getInstance().addDeviceControl(tUuid, tKioskControl);
                    } catch (NetworkException iException) {
                        tNetwork.emitError("Could not register kiosk service", iException);
                    }
                }
                if ((tService = iDevice.findService(ApplicationControl.ServiceId)) != null) {
                    getLogger().debug("Registering media service");
                    try {
                        ApplicationControl tMediaControl = new ApplicationControl(tService);
                        Network.getInstance().addApplicationControl(tUuid, tMediaControl);
                    } catch (NetworkException iException) {
                        tNetwork.emitError("Could not register media service", iException);                    }
                }
            }

            // Device removal
            @Override
            public void remoteDeviceRemoved(Registry iRegistry, RemoteDevice iDevice) {
                Network tNetwork = Network.getInstance();
                getLogger().debug("Device removed from registry: " + iDevice.getDisplayString());
                UUID tUuid;
                try {
                    tUuid = _convertUdn(iDevice.getIdentity().getUdn());
                } catch (NetworkException iException) {
                    tNetwork.emitError("Cannot proceed without having the device identifier", iException);
                    return;
                }

                // Scan for services
                RemoteService tService;
                if ((tService = iDevice.findService(DeviceControl.ServiceId)) != null) {
                    getLogger().debug("Removing kiosk control");
                    try {
                        Network.getInstance().removeDeviceControl(tUuid);
                    } catch (NetworkException iException) {
                        tNetwork.emitError("Could not remove kiosk control", iException);
                    }
                }
                if ((tService = iDevice.findService(ApplicationControl.ServiceId)) != null) {
                    getLogger().debug("Removing media control");
                    try {
                        Network.getInstance().removeApplicationControl(tUuid);
                    } catch (NetworkException iException) {
                        tNetwork.emitError("Could not remove media control", iException);
                    }
                }
            }
        };
    }
    
    
    //
    // Auxiliary
    //
    
    UUID _convertUdn(UDN iUDN) throws NetworkException {
        if (! iUDN.isUDA11Compliant())
            throw new NetworkException("cannot convert incompatible UDN");
        return UUID.fromString(iUDN.getIdentifierString());
    }

}
