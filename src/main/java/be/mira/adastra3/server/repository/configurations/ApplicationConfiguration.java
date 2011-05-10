/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.exceptions.RepositoryException;

/**
 *
 * @author tim
 */
public class ApplicationConfiguration implements IConfiguration {
    //
    // Data members
    //
    
    private Media mMedia;
    private Interface mInterface;
    
    
    //
    // Auxiliry classes
    //
    
    public class Interface implements IConfiguration  {
        private String mLocation;
    
        @Override
        public void check() throws RepositoryException {
            // TODO: location should be present, unless abstract...
        }
        
        String getLocation() {
            return mLocation;
        }
        
        public void setLocation(String iLocation) {
            mLocation = iLocation;
        }
    }
    
    public class Media implements IConfiguration  {
        private String mLocation;
    
        @Override
        public void check() throws RepositoryException {
            // TODO: location should be present, unless abstract...
        }
        
        String getLocation() {
            return mLocation;
        }
        
        public void setLocation(String iLocation) {
            mLocation = iLocation;
        }
    }
    
    
    //
    // Getters and setters
    //
    
    @Override
    public void check() throws RepositoryException {
        
    }

    public Interface getInterface() {
        return mInterface;
    }

    public void setInterface(Interface iInterface) {
        mInterface = iInterface;
    }

    public Media getMedia() {
        return mMedia;
    }

    public void setMedia(Media iMedia) {
        mMedia = iMedia;
    }
}
