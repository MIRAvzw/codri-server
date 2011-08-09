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
    
    public Kiosk30(final UUID iUuid, final DeviceControl iDeviceControle, final ApplicationControl iApplicationControl) {
        super(iUuid);
        
        mDeviceControl = iDeviceControle;
        mApplicationControl = iApplicationControl;
    }
    
    
    //
    // Configuration handling
    //
    
    @Override
    public final void setConfiguration(final Configuration iConfiguration) throws DeviceException {
        // Check the configuration type
        if (!(iConfiguration instanceof KioskConfiguration)) {
            throw new DeviceException("device does not support non-kioskconfiguration propagation");
        }
        KioskConfiguration tKioskConfiguration = (KioskConfiguration) iConfiguration;
        
        // Manage the device
        DeviceConfiguration tDeviceConfiguration = tKioskConfiguration.getDeviceConfiguration();
        try {
            getDeviceControl().setVolume(tDeviceConfiguration.getSoundConfiguration().getVolume());
        } catch (NetworkException tException) {
            throw new DeviceException("could not propagate device configuration", tException);
        }
        
        // Manage the application
        ApplicationConfiguration tApplicationConfiguration = tKioskConfiguration.getApplicationConfiguration();
        try {
            // TODO: check revision, e.d.
            Repository tRepository = Repository.getInstance();
            
            InterfaceConfiguration tInterfaceConfiguration = tApplicationConfiguration.getInterfaceConfiguration();
            if (tInterfaceConfiguration != null) {
                String tInterfaceLocation = tRepository.getServer()
                        + "/interfaces/" + tInterfaceConfiguration.getId();
                getApplicationControl().loadInterface(tInterfaceConfiguration.getId(), tInterfaceConfiguration.getRole(), tInterfaceLocation);                
            }
            
            MediaConfiguration tMediaConfiguration =  tApplicationConfiguration.getMediaConfiguration();
            if (tMediaConfiguration != null) {
                String tMediaLocation = tRepository.getServer()
                        + "/media/" + tMediaConfiguration.getId();
                getApplicationControl().loadMedia(tMediaConfiguration.getId(), tMediaLocation);
            }
        } catch (NetworkException tException) {
            throw new DeviceException("could not propagate device configuration", tException);
        }
    }
    
    
    //
    // Getters and setters
    //
    
    public final ApplicationControl getApplicationControl() {
        return mApplicationControl;
    }
    
    public final DeviceControl getDeviceControl() {
        return mDeviceControl;
    }
}
