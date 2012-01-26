/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.events;

import be.mira.adastra3.server.repository.RepositoryEntity;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

/**
 *
 * @author tim
 */
public abstract class RepositoryEvent extends ApplicationEvent {
    //
    // Public types
    //
    
    public enum RepositoryEventType {
        ADDED,
        UPDATED,
        REMOVED
    }
    
    
    //
    // Member data
    //
    
    private final String mId;
    private final RepositoryEvent.RepositoryEventType mType;
    private final RepositoryEntity mEntity, mOldEntity;
    
    
    //
    // Construction and destruction
    //
    
    public RepositoryEvent(Object iSource, RepositoryEvent.RepositoryEventType iType, String iId, RepositoryEntity iEntity) {
        this(iSource, iType, iId, iEntity, null);
    }
    
    public RepositoryEvent(Object iSource, RepositoryEvent.RepositoryEventType iType, String iId, RepositoryEntity iEntity, RepositoryEntity iOldEntity) {
        super(iSource);
        mType = iType;
        mId = iId;
        mEntity = iEntity;
        mOldEntity = iOldEntity;
        Assert.isTrue(mOldEntity == null || mType == RepositoryEventType.UPDATED);
    }
    
    
    //
    // Basic I/O
    //
    
    public final RepositoryEvent.RepositoryEventType getType() {
        return mType;
    }
    
    public final String getId() {
        return mId;
    }
    
    public final RepositoryEntity getEntity() {
        return mEntity;
    }
    
    public final RepositoryEntity getOldEntity() {
        return mOldEntity;
    }    
}
