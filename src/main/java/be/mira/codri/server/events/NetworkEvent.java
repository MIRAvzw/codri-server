/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.events;

import be.mira.codri.server.bo.network.NetworkEntity;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author tim
 */
public abstract class NetworkEvent extends ApplicationEvent {
    //
    // Public types
    //
    
    public enum NetworkEventType {
        ADDED,
        REFRESHED,
        EXPIRED,
        REMOVED
    }
    
    
    //
    // Member data
    //
    
    private final NetworkEventType mType;
    private final String mId;
    private final NetworkEntity mEntity, mOldEntity;
    
    
    //
    // Construction and destruction
    //
    
    public NetworkEvent(final Object iSource, final NetworkEventType iType, final String iId, final NetworkEntity iEntity) {
        this(iSource, iType, iId, iEntity, null);
    }
    
    public NetworkEvent(final Object iSource, final NetworkEventType iType, final String iId, final NetworkEntity iEntity, final NetworkEntity iOldEntity) {
        super(iSource);
        mType = iType;
        mId = iId;
        mEntity = iEntity;
        mOldEntity = iOldEntity;
    }
    
    
    //
    // Basic I/O
    //
    
    public final String getId() {
        return mId;
    }
    
    public final NetworkEventType getType() {
        return mType;
    }
    
    public final NetworkEntity getEntity() {
        return mEntity;
    }
    
    public final NetworkEntity getOldEntity() {
        return mOldEntity;
    }
    
}
