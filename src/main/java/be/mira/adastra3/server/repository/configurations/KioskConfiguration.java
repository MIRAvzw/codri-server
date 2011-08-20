/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.repository.configurations.objects.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.objects.MediaConfiguration;

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
    
    public KioskConfiguration(final String iId, final DeviceConfiguration iDeviceConfiguration, final MediaConfiguration iMediaConfiguration) {
        super(iId);
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
