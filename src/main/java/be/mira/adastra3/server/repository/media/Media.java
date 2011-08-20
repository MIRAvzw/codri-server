/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.media;

/**
 *
 * @author tim
 */
public class Media {
    //
    // Member data
    //
    
    private String mId;
    private Long mRevision;
    
    
    //
    // Construction and destruction
    //
    
    public Media(final String iId) {
        mId = iId;
    }
    
    
    //
    // Getters and setters
    //

    public final String getId() {
        return mId;
    }
    
    public final long getRevision() {
        return mRevision;
    }
    
    public final void setRevision(final long iRevision) {
        mRevision = iRevision;
    }
}
