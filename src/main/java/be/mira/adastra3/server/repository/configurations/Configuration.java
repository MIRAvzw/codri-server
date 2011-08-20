/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import java.util.UUID;

/**
 *
 * @author tim
 */
public abstract class Configuration {
    //
    // Member data
    //
    
    private String mId;
    private UUID mTarget;
    private Long mRevision;
    
    
    //
    // Construction and destruction
    //
    
    public Configuration(final String iId, final UUID iTarget) {
        mId = iId;
        mTarget = iTarget;
    }
    
    
    //
    // Getters and setters
    //

    public final String getId() {
        return mId;
    }

    public final UUID getTarget() {
        return mTarget;
    }

    public final void setTarget(final UUID iTarget) {
        mTarget = iTarget;
    }
    
    public final long getRevision() {
        return mRevision;
    }
    
    public final void setRevision(final long iRevision) {
        mRevision = iRevision;
    }
}
