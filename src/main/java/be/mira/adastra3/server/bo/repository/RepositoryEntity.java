/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.bo.repository;

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
    private final String mServer; // FIXME: duplicate information
    
    
    //
    // Construction and destruction
    //
    
    public RepositoryEntity(final Long iRevision, final String iPath, final String iServer) {
        mRevision = iRevision;
        mPath = iPath;
        mServer = iServer;
    }
    
    
    //
    // Getters & setters
    //
    
    public final long getRevision() {
        return mRevision;
    }
    
    public final String getPath() {
        return mPath;
    }
    
    public final String getLocation() {
        return mServer + getPath();
    }    
}
