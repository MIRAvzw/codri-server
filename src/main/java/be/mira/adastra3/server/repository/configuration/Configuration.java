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
    
    private String mId;
    private SoundConfiguration mSoundConfiguration;
    
    //
    // Construction and destruction
    //
    
    public Configuration(final long iRevision, final String iPath, final String iId, final SoundConfiguration iSoundConfiguration) {
        super(iRevision, iPath);
        mId = iId;
        mSoundConfiguration = iSoundConfiguration;
    }
    
    
    //
    // Getters and setters
    //

    public final String getId() {
        return mId;
    }
    
    public final SoundConfiguration getSoundConfiguration() {
        return mSoundConfiguration;
    }
}
