/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.beans;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Kiosk;
import be.mira.adastra3.server.network.NetworkEntity;
import be.mira.adastra3.server.network.controls.ConfigurationControl;
import be.mira.adastra3.server.network.controls.PresentationControl;
import be.mira.adastra3.server.beans.factory.Logger;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.logging.Log;
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
public class NetworkMonitor {
    //
    // Data members
    //
    
    @Logger
    private Log mLogger;
    
    private Network mNetwork;
    
    
    //
    // Construction and destruction
    //

    public NetworkMonitor(Network iNetwork) {
        mNetwork = iNetwork;
    }
    
    @PostConstruct
    public final void init() throws Exception {
        // Add a listener for device registration events
        try {
            mNetwork.getUpnpService().getRegistry().addListener(
                    createRegistryListener(mNetwork.getUpnpService())
            );
        } catch (Exception tException) {
            throw new Exception(tException);
        }
        
        // Broadcast a search message for all devices
        mLogger.debug("Looking for devices");
        mNetwork.getUpnpService().getControlPoint().search(
                new STAllHeader()
        );
    }
    
    @PreDestroy
    public final void destroy() throws Exception {
        mNetwork.getUpnpService().shutdown();
    }
    
    final RegistryListener createRegistryListener(final UpnpService iService) {
        return new DefaultRegistryListener() {
            // Device addition
            @Override
            public void remoteDeviceAdded(final Registry iRegistry, final RemoteDevice iDevice) {
                // Check for MIRA devices
                mLogger.debug("New device entered the network: " + iDevice.getDisplayString());
                ManufacturerDetails tManufacturer = iDevice.getDetails().getManufacturerDetails();
                if (tManufacturer.getManufacturer().equals("Volkssterrenwacht MIRA vzw")) {                
                    // Get the device UUID
                    UUID tUuid = convertUdn(iDevice.getIdentity().getUdn());
                    if (tUuid == null) {
                        mLogger.warn("could not convert device UDN '" + iDevice.getIdentity().getUdn() + "' to a valid UUID");
                        return;
                    }

                    // Check the device type
                    ModelDetails tModel = iDevice.getDetails().getModelDetails();
                    if (tModel.getModelName().equals("Ad-Astra Kiosk") && tModel.getModelNumber().equals("3.0")) {
                        mLogger.debug("Identified device as Ad-Astra 3.0 model");
                        
                        try {                        
                            // Extract services
                            RemoteService tServiceDevice = iDevice.findService(ConfigurationControl.cIdentifier);
                            RemoteService tServiceApplication = iDevice.findService(PresentationControl.cIdentifier);
                            if (tServiceDevice == null || tServiceApplication == null) {
                                throw new NetworkException("an essential network service has not been found");
                            }
                            
                            // Create device
                            Kiosk tDevice = new Kiosk(tUuid, new ConfigurationControl(tServiceDevice, mNetwork.getControlPoint()), new PresentationControl(tServiceApplication, mNetwork.getControlPoint()));
                            tDevice.setName(iDevice.getDetails().getFriendlyName());
                            mNetwork.addDevice(tDevice);
                        } catch (NetworkException tException) {
                            mLogger.error("could not register Ad-Astra 3.0 device", tException);
                            return;
                        }
                    } else {
                        mLogger.warn("an unknown MIRA device has been detected: " + tModel.getModelName() + " " + tModel.getModelNumber());
                        return;
                    }
                }
            }

            // Device removal
            @Override
            public void remoteDeviceRemoved(final Registry iRegistry, final RemoteDevice iDevice) {
                // Check for MIRA devices
                mLogger.debug("Device exited the network: " + iDevice.getDisplayString());
                ManufacturerDetails tManufacturer = iDevice.getDetails().getManufacturerDetails();
                if (tManufacturer.getManufacturer().equals("Volkssterrenwacht MIRA vzw")) {                
                    // Get the device UUID
                    UUID tUuid = convertUdn(iDevice.getIdentity().getUdn());
                    if (tUuid == null) {
                        mLogger.warn("could not convert device UDN '" + iDevice.getIdentity().getUdn() + "' to a valid UUID");
                        return;
                    }
                    
                    // Remove the device
                    try {
                        NetworkEntity tDevice = mNetwork.getDevice(tUuid); 
                        if (tDevice != null) {
                            mNetwork.removeDevice(tDevice);
                        }
                    } catch (NetworkException tException) {
                        mLogger.error("could not remove a MIRA device", tException);
                    }
                }
            }
            
            // Device update

            @Override
            public void remoteDeviceUpdated(Registry iRegistry, RemoteDevice iDevice) {
                // Check for MIRA devices
                mLogger.debug("Device in the network got updated: " + iDevice.getDisplayString());
                ManufacturerDetails tManufacturer = iDevice.getDetails().getManufacturerDetails();
                if (tManufacturer.getManufacturer().equals("Volkssterrenwacht MIRA vzw")) {                
                    // Get the device UUID
                    UUID tUuid = convertUdn(iDevice.getIdentity().getUdn());
                    if (tUuid == null) {
                        mLogger.warn("could not convert device UDN '" + iDevice.getIdentity().getUdn() + "' to a valid UUID");
                        return;
                    }
                    
                    // Update the device
                    NetworkEntity tDevice = mNetwork.getDevice(tUuid);
                    if (tDevice != null) {
                        tDevice.setMark();
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
