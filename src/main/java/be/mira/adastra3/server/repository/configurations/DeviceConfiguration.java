/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.repository.configurations.device.SoundConfiguration;

/**
 *
 * @author tim
 */
public class DeviceConfiguration extends Configuration {
    //
    // Data members
    //
    
    private SoundConfiguration mSound;
    
    
    //
    // Construction and destruction
    //
    
    public DeviceConfiguration() {        
        setSoundConfiguration(new SoundConfiguration());
    }
    
    
    //
    // Getters and setters
    //
    
    public final SoundConfiguration getSoundConfiguration() {
        return mSound;
    }
    
    public final void setSoundConfiguration(final SoundConfiguration iSound) {
        if (iSound == null) {
            return;
        }
        mSound = iSound;
    }
}
