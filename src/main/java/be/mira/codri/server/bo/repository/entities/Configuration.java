/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.repository.entities;

import be.mira.codri.server.bo.repository.RepositoryEntity;
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
    
    private final Integer mVolume;
    
    
    //
    // Construction and destruction
    //
    
    public Configuration(final Long iRevision, final String iPath, final Integer iVolume) {
        super(iRevision, iPath);
        mVolume = iVolume;
    }
    
    
    //
    // Getters and setters
    //
    
    @XmlElement
    public Integer getVolume() {
        return mVolume;
    }
}
