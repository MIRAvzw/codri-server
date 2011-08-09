/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations.application;

import be.mira.adastra3.server.repository.configurations.Configuration;

/**
 *
 * @author tim
 */
public class MediaConfiguration extends Configuration  {
    //
    // Data members
    //
    
    private String mId;
    
    
    //
    // Construction and destruction
    //
    
    public MediaConfiguration(final String iId) {
        mId = iId;
    }
    
    
    //
    // Getters and setters
    //
    
    public final String getId() {
        return mId;
    }   
}
