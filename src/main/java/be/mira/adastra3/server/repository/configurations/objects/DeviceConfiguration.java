/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations.objects;

/**
 *
 * @author tim
 */
public final class DeviceConfiguration {
    //
    // Data members
    //
    
    private SoundConfiguration mSound;
    
    
    //
    // Constructors
    //
    
    public DeviceConfiguration(final SoundConfiguration iSound) {
        mSound = iSound;
    }
    
    
    //
    // Getters
    //
    
    public SoundConfiguration getSound() {
        return mSound;
    }
}
