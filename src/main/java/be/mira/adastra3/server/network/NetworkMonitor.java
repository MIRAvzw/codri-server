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
import java.util.Timer;
import java.util.TimerTask;
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
    // Data members
    //
    
    private Timer mAliveMonitor;
    private int mAliveMonitorInterval;
    
    //
    // Auxiliary classes
    //

    private class Monitor extends TimerTask {
        @Override
        public void run() {
            getLogger().debug("Checking device availability");
            
            for (Device tDevice : Network.getInstance().getDevices()) {
                if (tDevice instanceof Kiosk30) {
                    Kiosk30 tKiosk = (Kiosk30) tDevice;
                    try {
                        long tLatency = tKiosk.getDeviceControl().ping();
                        getLogger().debug("Latency for kiosk " + tKiosk.getUuid() + ": " + tLatency + " ms");
                    } catch (NetworkException tException) {
                        getLogger().warn("Could not check kiosk " + tKiosk.getUuid());
                        // TODO: mark this kiosk as temporarily unavailable
                        //       when a new config/media gets there, we can't straight
                        //       away send the data, but should queue it untill the
                        //       kiosk comes back to life again (or gets completely
                        //       unsubscribed, in which case we can drop the
                        //       changes)
                    }
                }
            }
        }
    }

    //
    // Construction and destruction
    //

    public NetworkMonitor() throws ServiceSetupException {
        super();
        
        // Add a listener for device registration events
        try {
            Network.getInstance().getUpnpService().getRegistry().addListener(
                    createRegistryListener(Network.getInstance().getUpnpService())
            );
        } catch (Exception tException) {
            throw new ServiceSetupException(tException);
        }

        // Monitor timer
        Integer tInterval = Integer.parseInt(getProperty("interval", "60"));
        if (tInterval <= 0) {
            throw new ServiceSetupException("Update interval out of valid range");
        }
        mAliveMonitorInterval = tInterval;
        getLogger().debug("Scheduling SVN monitor with interval of " + tInterval + " seconds");
        mAliveMonitor = new Timer();
    }


    //
    // Service interface
    //

    @Override
    public final void run() throws ServiceRunException {
        // Broadcast a search message for all devices
        try {
            getLogger().debug("Looking for devices");
            Network.getInstance().getUpnpService().getControlPoint().search(
                    new STAllHeader()
            );
        } catch (Exception tException) {
            throw new ServiceRunException(tException);
        }

        // Schedule the monitor
        mAliveMonitor.schedule(new Monitor(), 0, mAliveMonitorInterval * 1000);
    }

    @Override
    public final void stop() throws ServiceRunException {
        try {
            Network.getInstance().getUpnpService().shutdown();
        } catch (Exception tException) {
            throw new ServiceRunException(tException);
        }
    }
    
    final RegistryListener createRegistryListener(final UpnpService iService) {
        return new DefaultRegistryListener() {
            // Device addition
            @Override
            public void remoteDeviceAdded(final Registry iRegistry, final RemoteDevice iDevice) {
                // Check for MIRA devices
                getLogger().debug("New device entered the network: " + iDevice.getDisplayString());
                ManufacturerDetails tManufacturer = iDevice.getDetails().getManufacturerDetails();
                if (tManufacturer.getManufacturer().equals("Volkssterrenwacht MIRA vzw")) {                
                    // Get the device UUID
                    Network tNetwork = Network.getInstance();
                    UUID tUuid = convertUdn(iDevice.getIdentity().getUdn());
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
                            RemoteService tServiceDevice = iDevice.findService(DeviceControl.cIdentifier);
                            RemoteService tServiceApplication = iDevice.findService(ApplicationControl.cIdentifier);
                            if (tServiceDevice == null || tServiceApplication == null) {
                                throw new NetworkException("an essential network service has not been found");
                            }
                            
                            // Create device
                            Kiosk30 tDevice = new Kiosk30(tUuid, new DeviceControl(tServiceDevice), new ApplicationControl(tServiceApplication));
                            tDevice.setName(iDevice.getDetails().getFriendlyName());
                            tNetwork.addDevice(tDevice);
                        } catch (NetworkException tException) {
                            tNetwork.emitError("could not register Ad-Astra 3.0 device", tException);
                            return;
                        }
                    } else {
                        tNetwork.emitWarning("an unknown MIRA device has been detected: " + tModel.getModelName() + " " + tModel.getModelNumber());
                        return;
                    }
                }
            }

            // Device removal
            @Override
            public void remoteDeviceRemoved(final Registry iRegistry, final RemoteDevice iDevice) {
                // Check for MIRA devices
                getLogger().debug("Device exited the network: " + iDevice.getDisplayString());
                ManufacturerDetails tManufacturer = iDevice.getDetails().getManufacturerDetails();
                if (tManufacturer.getManufacturer().equals("Volkssterrenwacht MIRA vzw")) {                
                    // Get the device UUID
                    Network tNetwork = Network.getInstance();
                    UUID tUuid = convertUdn(iDevice.getIdentity().getUdn());
                    if (tUuid == null) {
                        tNetwork.emitWarning("could not convert device UDN '" + iDevice.getIdentity().getUdn() + "' to a valid UUID");
                        return;
                    }
                    
                    // Remove the device (without type checking, so this might croak ocasionally)
                    try {
                        Device tDevice = tNetwork.getDevice(tUuid); 
                        tNetwork.removeDevice(tDevice);
                    } catch (NetworkException tException) {
                        tNetwork.emitError("could not remove a MIRA device", tException);
                    }
                }
            }
        };
    }
    
    
    //
    // Auxiliary
    //
    
    private final static Pattern cUuidPattern = Pattern.compile("[\\p{XDigit}]{8}-[\\p{XDigit}]{4}-[\\p{XDigit}]{4}-[\\p{XDigit}]{4}-[\\p{XDigit}]{12}");
    private UUID convertUdn(final UDN iUdn) {
        Matcher tUuidMatcher = cUuidPattern.matcher(iUdn.getIdentifierString());
        if (tUuidMatcher.find()) {
            return UUID.fromString(tUuidMatcher.group());
        } else {
            return null;
        }
    }

}
