/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.repository;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author tim
 */
public abstract class RepositoryEntity {
    //
    // Member data
    //
    
    private final Long mRevision;
    private final String mLocation;
    
    
    //
    // Construction and destruction
    //
    
    public RepositoryEntity(final Long iRevision, final String iLocation) {
        mRevision = iRevision;
        mLocation = iLocation;
    }
    
    
    //
    // Basic I/O
    //
    
    @XmlElement
    public final long getRevision() {
        return mRevision;
    }
    
    @XmlElement
    public final String getLocation() {
        return mLocation;
    }
}
