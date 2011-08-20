/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.entities;

import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.MediaControl;
import be.mira.adastra3.server.network.controls.DeviceControl;
import be.mira.adastra3.server.repository.Repository;
import be.mira.adastra3.server.repository.configurations.objects.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.objects.MediaConfiguration;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class Kiosk extends Entity {
    //
    // Member data
    //
    
    private MediaControl mApplicationControl;
    private DeviceControl mDeviceControl;
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk(final UUID iUuid, final DeviceControl iDeviceControle, final MediaControl iApplicationControl) {
        super(iUuid);
        
        mDeviceControl = iDeviceControle;
        mApplicationControl = iApplicationControl;
    }
    
    
    //
    // Configuration handling
    //
    
    public final void setDeviceConfiguration(final DeviceConfiguration iDeviceConfiguration) throws DeviceException {
        try {
            getDeviceControl().setVolume(iDeviceConfiguration.getSoundConfiguration().getVolume());
        } catch (NetworkException tException) {
            throw new DeviceException("could not push the device settings", tException);
        }        
    }
    
    public final void setMediaConfiguration(final MediaConfiguration iMediaConfiguration) throws DeviceException {
        try {
            // TODO: check revision, e.d.
            Repository tRepository = Repository.getInstance();
            
            String tIdentifier =  iMediaConfiguration.getIdentifier();
            if (tIdentifier != null) {
                String tLocation = tRepository.getServer()
                        + "/media/" + tIdentifier;
                getMediaControl().setLocation(tLocation);
            }
        } catch (NetworkException tException) {
            throw new DeviceException("could not push the device settings", tException);
        }        
    }
    
    
    //
    // Getters and setters
    //
    
    public final MediaControl getMediaControl() {
        return mApplicationControl;
    }
    
    public final DeviceControl getDeviceControl() {
        return mDeviceControl;
    }
}
