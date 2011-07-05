/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.repository.configurations.application.MediaConfiguration;
import be.mira.adastra3.server.repository.configurations.application.InterfaceConfiguration;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
public class ApplicationConfiguration extends Configuration {
    //
    // Data members
    //
    
    private List<MediaConfiguration> mMediaConfigurations;
    private List<InterfaceConfiguration> mInterfaceConfigurations;
    
    
    //
    // Construction and destruction
    //
    
    public ApplicationConfiguration() {
        mInterfaceConfigurations = new ArrayList<InterfaceConfiguration>();
        mMediaConfigurations = new ArrayList<MediaConfiguration>();
    }
    
    
    //
    // Getters and setters
    //

    public List<InterfaceConfiguration> getInterfaceConfigurations() {
        return mInterfaceConfigurations;
    }

    public final void addInterfaceConfiguration(InterfaceConfiguration iInterface) {
        mInterfaceConfigurations.add(iInterface);
    }

    public List<MediaConfiguration> getMediaConfigurations() {
        return mMediaConfigurations;
    }

    public final void addMediaConfiguration(MediaConfiguration iMedia) {
        mMediaConfigurations.add(iMedia);
    }
}
