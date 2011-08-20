/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations.objects;

import be.mira.adastra3.server.repository.configurations.Configuration;

/**
 *
 * @author tim
 */
public class DeviceConfiguration {
    //
    // Data members
    //
    
    private SoundConfiguration mSoundConfiguration;
    
    
    //
    // Constructors
    //
    
    public DeviceConfiguration(SoundConfiguration iSoundConfiguration) {
        mSoundConfiguration = iSoundConfiguration;
    }
    
    
    //
    // Getters
    //
    
    public SoundConfiguration getSoundConfiguration() {
        return mSoundConfiguration;
    }
}
