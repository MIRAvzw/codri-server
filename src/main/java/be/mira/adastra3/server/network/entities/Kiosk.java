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
import be.mira.adastra3.server.repository.configurations.Configuration;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import be.mira.adastra3.server.repository.configurations.objects.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.objects.MediaConfiguration;
import be.mira.adastra3.server.repository.media.Media;
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
    
    public final void setConfiguration(final Configuration iConfiguration) throws DeviceException {
        if (!(iConfiguration instanceof KioskConfiguration)) {
            throw new DeviceException("kiosk can only be configured with a kioskconfiguration");
        }
        final KioskConfiguration tKioskConfiguration = (KioskConfiguration) iConfiguration;
        
        long tOldKioskConfigurationRevision;
        try {
            tOldKioskConfigurationRevision = this.getDeviceControl().getRevision();
        } catch(NetworkException tException) {
            throw new DeviceException("could not check configuration revision", tException);
        }
        
        if (tKioskConfiguration.getRevision() != tOldKioskConfigurationRevision) {
            setDeviceConfiguration(tKioskConfiguration.getDeviceConfiguration());
            setMediaConfiguration(tKioskConfiguration.getMediaConfiguration());
            
            // TODO: this is kind of a mess... there are two revision numbers,
            //       on for the currently loaded media, and one for the 
            //       configuration. The media is properly saved within the
            //       mediacontrol, but the configuration one is saved
            //       within the devicecontrol. as I said, kind of a mess
            try {
                getDeviceControl().setRevision(tKioskConfiguration.getRevision());
            } catch (NetworkException tException) {
                throw new DeviceException("could not set the device revision", tException);
            }
        }
    }
    
    public final void setDeviceConfiguration(final DeviceConfiguration iDeviceConfiguration) throws DeviceException {
        try {
            getDeviceControl().setVolume(iDeviceConfiguration.getSoundConfiguration().getVolume());
        } catch (NetworkException tException) {
            throw new DeviceException("could not push the device settings", tException);
        }
    }
    
    public final void setMediaConfiguration(final MediaConfiguration iMediaConfiguration) throws DeviceException {
        Repository tRepository = Repository.getInstance();

        // Not pushing the media location here because that is taken care of
        // by setMedia
        // TODO: this is fugly
        // TODO: how to fix this? Don't push configuration through device control!
    }
    
    
    //
    // Media handling
    //
    
    public final void setMedia(final Media iMedia) throws DeviceException {        
        long tOldMediaRevision;
        try {
            tOldMediaRevision = getMediaControl().getRevision();
        } catch(NetworkException tException) {
            throw new DeviceException("could not check media revision", tException);
        }
        
        if (iMedia.getRevision() != tOldMediaRevision) {
            try {
                getMediaControl().setLocation(iMedia.getLocation());
            } catch (NetworkException tException) {
                throw new DeviceException("could not set the media location", tException);
            }
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
