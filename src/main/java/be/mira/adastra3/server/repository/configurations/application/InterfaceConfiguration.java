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
public class InterfaceConfiguration extends Configuration {
    //
    // Data members
    //
    
    private String mId;
    
    
    //
    // Construction and destruction
    //
    
    public InterfaceConfiguration(String iId) {
        mId = iId;
    }
    
    
    //
    // Getters and setters
    //
    
    public String getId() {
        return mId;
    }
}
