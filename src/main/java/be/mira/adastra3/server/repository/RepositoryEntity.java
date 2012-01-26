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
    
    private final Long mRevision;
    private final String mPath;
    private final String mServer; // FIXME: duplicate information
    
    
    //
    // Construction and destruction
    //
    
    // FIXME: dummy constructor for JAXB (shouldn't be neccesary as JAXB never
    // has to unmarshal this class)
    public RepositoryEntity() {
        throw new UnsupportedOperationException();
    }
    
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
