/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.events;

import be.mira.adastra3.server.network.NetworkEntity;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author tim
 */
public class NetworkEvent extends ApplicationEvent {
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
    private final NetworkEntity mDevice;
    
    
    //
    // Construction and destruction
    //
    
    public NetworkEvent(Object iSource, NetworkEventType iType, NetworkEntity iDevice) {
        super(iSource);
        mType = iType;
        mDevice = iDevice;
    }
    
    
    //
    // Basic I/O
    //
    
    public final NetworkEventType getType() {
        return mType;
    }
    
    public final NetworkEntity getDevice() {
        return mDevice;
    }
    
}
