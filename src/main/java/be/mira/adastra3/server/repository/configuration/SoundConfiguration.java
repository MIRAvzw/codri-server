/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configuration;

/**
 *
 * @author tim
 */
public final class SoundConfiguration {
    //
    // Data members
    //
    
    private Integer mVolume;
    
    
    //
    // Constructors
    //
    
    public SoundConfiguration(final Integer iVolume) {
        mVolume = iVolume;
    }
    
    
    //
    // Getters
    //
    
    public Integer getVolume() {
        return mVolume;
    }
}
