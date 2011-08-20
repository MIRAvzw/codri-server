/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations.objects;

/**
 *
 * @author tim
 */
public class ApplicationConfiguration {
    //
    // Data members
    //
    
    private MediaConfiguration mMedia;
    
    
    //
    // Constructors
    //
    
    public ApplicationConfiguration(final MediaConfiguration iMedia) {
        mMedia = iMedia;
    }
    
    
    //
    // Getters
    //
    
    public MediaConfiguration getMedia() {
        return mMedia;
    }
    
}
