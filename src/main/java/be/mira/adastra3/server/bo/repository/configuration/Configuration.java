/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.bo.repository.configuration;

import be.mira.adastra3.server.bo.repository.RepositoryEntity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tim
 */
@XmlRootElement(name="configuration")
public class Configuration extends RepositoryEntity {
    //
    // Member data
    //
    
    private SoundConfiguration mSoundConfiguration;
    
    //
    // Construction and destruction
    //
    
    public Configuration(final Long iRevision, final String iPath, final String iServer, final SoundConfiguration iSoundConfiguration) {
        super(iRevision, iPath, iServer);
        mSoundConfiguration = iSoundConfiguration;
    }
    
    
    //
    // Getters and setters
    //
    
    @XmlElement
    public final SoundConfiguration getSoundConfiguration() {
        return mSoundConfiguration;
    }
}
