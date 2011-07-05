/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.devices;

import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.ApplicationControl;
import be.mira.adastra3.server.network.controls.DeviceControl;
import be.mira.adastra3.server.repository.Repository;
import be.mira.adastra3.server.repository.configurations.ApplicationConfiguration;
import be.mira.adastra3.server.repository.configurations.Configuration;
import be.mira.adastra3.server.repository.configurations.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import be.mira.adastra3.server.repository.configurations.application.InterfaceConfiguration;
import be.mira.adastra3.server.repository.configurations.application.MediaConfiguration;
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
            Repository tRepository = Repository.getInstance();
            
            for (InterfaceConfiguration tInterfaceConfiguration: tApplicationConfiguration.getInterfaceConfigurations()) {
                String tInterface = tRepository.getServer()
                        + "/" + tInterfaceConfiguration.getLocation()
                        + "/" + tInterfaceConfiguration.getId();
                getApplicationControl().LoadInterface(tInterface);
            }
            getApplicationControl().ShowInterface();
            
            for (MediaConfiguration tMediaConfiguration: tApplicationConfiguration.getMediaConfigurations()) {
                String tMedia = tRepository.getServer()
                        + "/" + tMediaConfiguration.getLocation()
                        + "/" + tMediaConfiguration.getId();
                getApplicationControl().LoadMedia(tMedia);
            }
            getApplicationControl().ShowMedia();
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
