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
import be.mira.adastra3.server.network.devices.Device;
import be.mira.adastra3.server.network.devices.Kiosk30;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.teleal.cling.UpnpService;
import org.teleal.cling.model.message.header.STAllHeader;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
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
                // Check for MIRA devices
                getLogger().debug("New device entered the network: " + iDevice.getDisplayString());
                ManufacturerDetails tManufacturer = iDevice.getDetails().getManufacturerDetails();
                if (tManufacturer.getManufacturer().equals("Volkssterrenwacht MIRA vzw")) {                
                    // Get the device UUID
                    Network tNetwork = Network.getInstance();
                    UUID tUuid = _convertUdn(iDevice.getIdentity().getUdn());
                    if (tUuid == null) {
                        tNetwork.emitWarning("could not convert device UDN '" + iDevice.getIdentity().getUdn() + "' to a valid UUID");
                        return;
                    }

                    // Check the device type
                    ModelDetails tModel = iDevice.getDetails().getModelDetails();
                    if (tModel.getModelName().equals("Ad-Astra Kiosk") && tModel.getModelNumber().equals("3.0")) {
                        getLogger().debug("Identified device as Ad-Astra 3.0 model");
                        
                        try {                        
                            // Extract services
                            RemoteService tServiceDevice = iDevice.findService(DeviceControl.ServiceId);
                            RemoteService tServiceApplication = iDevice.findService(ApplicationControl.ServiceId);
                            if (tServiceDevice == null || tServiceApplication == null)
                                throw new NetworkException("an essential network service has not been found");
                            
                            // Create device
                            Kiosk30 tDevice = new Kiosk30(tUuid, new DeviceControl(tServiceDevice), new ApplicationControl(tServiceApplication));
                            tDevice.setName(iDevice.getDetails().getFriendlyName());
                            tNetwork.addDevice(tDevice);
                        } catch (NetworkException iException) {
                            tNetwork.emitError("could not register Ad-Astra 3.0 device", iException);
                            return;
                        }
                    }
                    else {
                        tNetwork.emitWarning("an unknown MIRA device has been detected: " + tModel.getModelName() + " " + tModel.getModelNumber());
                        return;
                    }
                }
            }

            // Device removal
            @Override
            public void remoteDeviceRemoved(Registry iRegistry, RemoteDevice iDevice) {
                // Check for MIRA devices
                getLogger().debug("Device exited the network: " + iDevice.getDisplayString());
                ManufacturerDetails tManufacturer = iDevice.getDetails().getManufacturerDetails();
                if (tManufacturer.getManufacturer().equals("Volkssterrenwacht MIRA vzw")) {                
                    // Get the device UUID
                    Network tNetwork = Network.getInstance();
                    UUID tUuid = _convertUdn(iDevice.getIdentity().getUdn());
                    if (tUuid == null) {
                        tNetwork.emitWarning("could not convert device UDN '" + iDevice.getIdentity().getUdn() + "' to a valid UUID");
                        return;
                    }
                    
                    // Remove the device (without type checking, so this might croak ocasionally)
                    try {
                        Device tDevice = tNetwork.getDevice(tUuid); 
                        tNetwork.removeDevice(tDevice);
                    } catch (NetworkException iException) {
                        tNetwork.emitError("could not remove a MIRA device", iException);
                    }
                }
            }
        };
    }
    
    
    //
    // Auxiliary
    //
    
    private Pattern mUuidPattern = Pattern.compile("[\\p{XDigit}]{8}-[\\p{XDigit}]{4}-[\\p{XDigit}]{4}-[\\p{XDigit}]{4}-[\\p{XDigit}]{12}");
    UUID _convertUdn(UDN iUdn) {
        Matcher mUuidMatcher = mUuidPattern.matcher(iUdn.getIdentifierString());
        if (mUuidMatcher.find())
            return UUID.fromString(mUuidMatcher.group());
        else
            return null;
    }

}
