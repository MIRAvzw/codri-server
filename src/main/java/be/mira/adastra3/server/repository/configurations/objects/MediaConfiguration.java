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
    
    private String mName;
    
    
    //
    // Constructors
    //
    
    public MediaConfiguration(final String iName) {
        mName = iName;
    }
    
    
    //
    // Getters
    //
    
    public String getName() {
        return mName;
    }    
}
