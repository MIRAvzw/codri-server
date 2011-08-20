/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.repository.configurations.objects.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.objects.MediaConfiguration;
import java.util.UUID;

/**
 *
 * @author tim
 */
public final class KioskConfiguration extends Configuration {
    //
    // Data members
    //
    
    private DeviceConfiguration mDeviceConfiguration;
    private MediaConfiguration mMediaConfiguration;
    
    
    //
    // Construction and destruction
    //
    
    public KioskConfiguration(final String iId, final UUID iTarget, final DeviceConfiguration iDeviceConfiguration, final MediaConfiguration iMediaConfiguration) {
        super(iId, iTarget);
        mDeviceConfiguration = iDeviceConfiguration;
        mMediaConfiguration = iMediaConfiguration;
    }
    
    
    //
    // Getters
    //
    
    public final DeviceConfiguration getDeviceConfiguration() {
        return mDeviceConfiguration;
    }
    
    public final MediaConfiguration getMediaConfiguration() {
        return mMediaConfiguration;
    }
}
