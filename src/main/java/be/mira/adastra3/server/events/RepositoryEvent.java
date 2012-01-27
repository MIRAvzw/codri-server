/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.events;

import be.mira.adastra3.server.bo.repository.RepositoryEntity;
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
    
    public RepositoryEvent(final Object iSource, final RepositoryEvent.RepositoryEventType iType, final String iId, final RepositoryEntity iEntity) {
        this(iSource, iType, iId, iEntity, null);
    }
    
    public RepositoryEvent(final Object iSource, final RepositoryEvent.RepositoryEventType iType, final String iId, final RepositoryEntity iEntity, final RepositoryEntity iOldEntity) {
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
