/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.bo.repository;

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
    private final String mPath;
    
    
    //
    // Construction and destruction
    //
    
    public RepositoryEntity(final Long iRevision, final String iPath) {
        mRevision = iRevision;
        mPath = iPath;
    }
    
    
    //
    // Getters & setters
    //
    
    @XmlElement
    public final long getRevision() {
        return mRevision;
    }
    
    @XmlElement
    public final String getPath() {
        return mPath;
    }
}
