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
public class DeviceConfiguration implements IConfiguration {
    //
    // Data members
    //
    
    private Sound mSound;
    
    
    //
    // Auxiliary classes
    //
    
    public class Sound implements IConfiguration {
        private Integer mVolume;
        
        @Override
        public void check() throws RepositoryException {
        if (getVolume() < 0 || getVolume() > 255)
            throw new RepositoryException("Volume value should fall between 0 and 255");

        }

        public int getVolume() {
            if (mVolume == null)
                return 0;
            return mVolume;
        }

        public void setVolume(int iVolume) {
            mVolume = iVolume;
        }
    }
    
    
    //
    // Getters and setters
    //
    
    @Override
    public void check() throws RepositoryException {
        getSound().check();
    }
    
    public Sound getSound() {
        if (mSound == null)
            return new Sound();
        return mSound;
    }
    
    public void setSound(Sound iSound) {
        mSound = iSound;
    }
}
