/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations.device;

import be.mira.adastra3.server.repository.configurations.Configuration;

/**
 *
 * @author tim
 */
public class SoundConfiguration extends Configuration {
    //
    // Construction and destruction
    //
    
    public SoundConfiguration() {
        setProperty("volume", 128);
    }
    
    
    //
    // Getters and setters
    //
    
    public final Integer getVolume() {
        if (getProperty("volume") != null) {
            return (Integer) getProperty("volume");
        } else {
            return null;
        }
    }

    public final void setVolume(final Integer iVolume) {
        setProperty("volume", iVolume);
    }
}
