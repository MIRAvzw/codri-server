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
import be.mira.adastra3.server.repository.configurations.Configuration;
import be.mira.adastra3.server.repository.configurations.Kiosk30Configuration;
import be.mira.adastra3.server.repository.configurations.objects.ApplicationConfiguration;
import be.mira.adastra3.server.repository.configurations.objects.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.objects.MediaConfiguration;
import be.mira.adastra3.server.repository.media.Media;
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
        if (!(iConfiguration instanceof Kiosk30Configuration)) {
            throw new DeviceException("device does not support non-kioskconfiguration propagation");
        }
        Kiosk30Configuration tKioskConfiguration = (Kiosk30Configuration) iConfiguration;
        
        // Check if the configuration isn't loaded yet
        // TODO: this doesn't belong in the ApplicationControl
        try {
            if (iConfiguration.getRevision() == getApplicationControl().getConfigurationRevision()) {
                return;
            }
        } catch (NetworkException tException) {
            throw new DeviceException("could not check existing configuration revision");
        }
        
        // Manage the device
        DeviceConfiguration tDeviceConfiguration = tKioskConfiguration.getDevice();
        try {
            getDeviceControl().setVolume(tDeviceConfiguration.getSound().getVolume());
        } catch (NetworkException tException) {
            throw new DeviceException("could not push the device settings", tException);
        }
        
        // Manage the application
        ApplicationConfiguration tApplicationConfiguration = tKioskConfiguration.getApplication();
        try {
            // TODO: check revision, e.d.
            Repository tRepository = Repository.getInstance();
            
            MediaConfiguration tMediaConfiguration =  tApplicationConfiguration.getMedia();
            if (tMediaConfiguration != null) {
                String tMediaLocation = tRepository.getServer()
                        + "/media/" + tMediaConfiguration.getIdentifier();
                Media tMedia = new Media(
                        tMediaConfiguration.getIdentifier(),
                        tMediaLocation);
                getApplicationControl().setMedia(tMedia);
            }
        } catch (NetworkException tException) {
            throw new DeviceException("could not push the device settings", tException);
        }
        
        // Save the configuration revision
        // TODO: this doesn't belong in the ApplicationControl
        try {
            getApplicationControl().setConfigurationRevision(iConfiguration.getRevision());
        } catch (NetworkException tException) {
            throw new DeviceException("could not push the configuration revision");
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
