/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations.objects;

/**
 *
 * @author tim
 */
public class MediaConfiguration {
    //
    // Data members
    //
    
    private String mIdentifier;
    
    
    //
    // Constructors
    //
    
    public MediaConfiguration(final String iIdentifier) {
        mIdentifier = iIdentifier;
    }
    
    
    //
    // Getters
    //
    
    public String getIdentifier() {
        return mIdentifier;
    }    
}
