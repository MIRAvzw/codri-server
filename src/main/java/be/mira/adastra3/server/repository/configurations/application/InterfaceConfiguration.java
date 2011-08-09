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
    private String mRole;
    
    
    //
    // Construction and destruction
    //
    
    public InterfaceConfiguration(final String iId, final String iRole) {
        mId = iId;
        mRole = iRole;
    }
    
    
    //
    // Getters and setters
    //
    
    public final String getId() {
        return mId;
    }
    
    public final String getRole() {
        return mRole;
    }
}
