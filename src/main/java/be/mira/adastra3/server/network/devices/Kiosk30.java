/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.devices;

import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.ApplicationControl;
import be.mira.adastra3.server.network.controls.DeviceControl;
import be.mira.adastra3.server.repository.configurations.ApplicationConfiguration;
import be.mira.adastra3.server.repository.configurations.Configuration;
import be.mira.adastra3.server.repository.configurations.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import be.mira.adastra3.server.repository.configurations.application.InterfaceConfiguration;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class Kiosk30 extends Device {
    //
    // Member data
    //
    
    private ApplicationControl mApplicationControl;
    private DeviceControl mDeviceControl;
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk30(UUID iUuid, DeviceControl iDeviceControle, ApplicationControl iApplicationControl) {
        super(iUuid);
        
        mDeviceControl = iDeviceControle;
        mApplicationControl = iApplicationControl;
    }
    
    
    //
    // Configuration handling
    //
    
    @Override
    public void setConfiguration(Configuration iConfiguration) throws DeviceException {
        // Check the configuration type
        if (!(iConfiguration instanceof KioskConfiguration))
            throw new DeviceException("device does not support non-kioskconfiguration propagation");
        KioskConfiguration iKioskConfiguration = (KioskConfiguration) iConfiguration;
        
        // Manage the device
        DeviceConfiguration tDeviceConfiguration = iKioskConfiguration.getDeviceConfiguration();
        try {
            getDeviceControl().SetVolume(tDeviceConfiguration.getSoundConfiguration().getVolume());
        }
        catch (NetworkException iException) {
            throw new DeviceException("could not propagate device configuration", iException);
        }
        
        // Manage the application
        ApplicationConfiguration tApplicationConfiguration = iKioskConfiguration.getApplicationConfiguration();
        try {
            // TODO: check revision, e.d.
            getApplicationControl().SetInterfaceLocation(tApplicationConfiguration.getInterfaceConfiguration().getLocation());
            getApplicationControl().LoadInterface();
            
            getApplicationControl().SetMediaLocation(tApplicationConfiguration.getMediaConfiguration().getLocation());
            getApplicationControl().LoadMedia();
        }
        catch (NetworkException iException) {
            throw new DeviceException("could not propagate device configuration", iException);
        }
    }
    
    
    //
    // Getters and setters
    //
    
    public ApplicationControl getApplicationControl() {
        return mApplicationControl;
    }
    
    public DeviceControl getDeviceControl() {
        return mDeviceControl;
    }
}
