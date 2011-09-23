/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository;

/**
 *
 * @author tim
 */
public abstract class RepositoryEntity {
    //
    // Member data
    //
    
    private final String mId;
    private final long mRevision;
    private final String mPath;
    
    
    //
    // Construction and destruction
    //
    
    public RepositoryEntity(final String iId, final long iRevision, final String iPath) {
        mId = iId;
        mRevision = iRevision;
        mPath = iPath;
    }
    
    
    //
    // Getters & setters
    //

    public final String getId() {
        return mId;
    }
    
    public final long getRevision() {
        return mRevision;
    }
    
    public final String getPath() {
        return mPath;
    }
    
}
