/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations.objects;

import java.util.UUID;

/**
 *
 * @author tim
 */
public class DeviceConfiguration {
    //
    // Data members
    //
    
    private UUID mIdentifier;
    private SoundConfiguration mSoundConfiguration;
    
    
    //
    // Constructors
    //
    
    public DeviceConfiguration(final UUID iIdentifier, SoundConfiguration iSoundConfiguration) {
        mIdentifier = iIdentifier;
        mSoundConfiguration = iSoundConfiguration;
    }
    
    
    //
    // Getters
    //
    
    public UUID getIdentifier() {
        return mIdentifier;
    }
    
    public SoundConfiguration getSoundConfiguration() {
        return mSoundConfiguration;
    }
}
