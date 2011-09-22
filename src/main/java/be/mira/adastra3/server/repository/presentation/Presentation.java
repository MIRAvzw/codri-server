/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.presentation;

import be.mira.adastra3.server.repository.Repository;
import be.mira.adastra3.server.repository.RepositoryEntity;

/**
 *
 * @author tim
 */
public class Presentation extends RepositoryEntity {
    //
    // Member data
    //
    
    private String mId;
    
    
    //
    // Construction and destruction
    //
    
    public Presentation(final long iRevision, final String iPath, final String iId) {
        super(iRevision, iPath);
        mId = iId;
    }
    
    
    //
    // Getters and setters
    //

    public final String getId() {
        return mId;
    }
    
    public final String getLocation() {
        return Repository.getInstance().getServer() + getPath();
    }
}
