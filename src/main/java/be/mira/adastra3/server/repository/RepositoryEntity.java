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
    
    private long mRevision;
    private String mPath;
    
    
    //
    // Construction and destruction
    //
    
    public RepositoryEntity(final long iRevision, final String iPath) {
        mRevision = iRevision;
        mPath = iPath;
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
    
}
