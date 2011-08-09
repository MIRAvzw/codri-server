/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.repository.configurations.application.MediaConfiguration;
import be.mira.adastra3.server.repository.configurations.application.InterfaceConfiguration;

/**
 *
 * @author tim
 */
public class ApplicationConfiguration extends Configuration {
    //
    // Data members
    //
    
    private MediaConfiguration mMediaConfiguration;
    private InterfaceConfiguration mInterfaceConfiguration;
    
    
    //
    // Construction and destruction
    //
    
    public ApplicationConfiguration() {
    }
    
    
    //
    // Getters and setters
    //

    public final InterfaceConfiguration getInterfaceConfiguration() {
        return mInterfaceConfiguration;
    }

    public final void setInterfaceConfiguration(final InterfaceConfiguration iInterfaceConfiguration) {
        mInterfaceConfiguration = iInterfaceConfiguration;
    }

    public final MediaConfiguration getMediaConfiguration() {
        return mMediaConfiguration;
    }

    public final void setMediaConfiguration(final MediaConfiguration iMediaConfiguration) {
        mMediaConfiguration = iMediaConfiguration;
    }
}
