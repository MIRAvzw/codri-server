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
        setMediaConfiguration(new MediaConfiguration());
        setInterfaceConfiguration(new InterfaceConfiguration());
    }
    
    
    //
    // Getters and setters
    //

    public InterfaceConfiguration getInterfaceConfiguration() {
        return mInterfaceConfiguration;
    }

    public final void setInterfaceConfiguration(InterfaceConfiguration iInterface) {
        if (iInterface == null)
            return;
        mInterfaceConfiguration = iInterface;
    }

    public MediaConfiguration getMediaConfiguration() {
        return mMediaConfiguration;
    }

    public final void setMediaConfiguration(MediaConfiguration iMedia) {
        if (iMedia == null)
            return;
        mMediaConfiguration = iMedia;
    }
}
