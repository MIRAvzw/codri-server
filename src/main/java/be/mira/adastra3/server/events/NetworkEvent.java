/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.events;

import be.mira.adastra3.server.network.NetworkEntity;
import java.util.UUID;
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
        REMOVED
    }
    
    
    //
    // Member data
    //
    
    private final NetworkEventType mType;
    private final UUID mId;
    private final NetworkEntity mEntity, mOldEntity;
    
    
    //
    // Construction and destruction
    //
    
    public NetworkEvent(Object iSource, NetworkEventType iType, UUID iId, NetworkEntity iEntity) {
        this(iSource, iType, iId, iEntity, null);
    }
    
    public NetworkEvent(Object iSource, NetworkEventType iType, UUID iId, NetworkEntity iEntity, NetworkEntity iOldEntity) {
        super(iSource);
        mType = iType;
        mId = iId;
        mEntity = iEntity;
        mOldEntity = iOldEntity;
    }
    
    
    //
    // Basic I/O
    //
    
    public final UUID getId() {
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
