/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configuration;

import be.mira.adastra3.server.repository.RepositoryEntity;

/**
 *
 * @author tim
 */
public class Configuration extends RepositoryEntity {
    //
    // Member data
    //
    
    private SoundConfiguration mSoundConfiguration;
    
    //
    // Construction and destruction
    //
    
    public Configuration(final String iId, final long iRevision, final String iPath, final String iServer, final SoundConfiguration iSoundConfiguration) {
        super(iId, iRevision, iPath, iServer);
        mSoundConfiguration = iSoundConfiguration;
    }
    
    
    //
    // Getters and setters
    //
    
    public final SoundConfiguration getSoundConfiguration() {
        return mSoundConfiguration;
    }
}
