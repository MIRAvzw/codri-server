/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.business;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.NetworkEntity;
import be.mira.adastra3.spring.Logger;
import be.mira.adastra3.server.events.NetworkEvent;
import be.mira.adastra3.server.events.NetworkEvent.NetworkEventType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * 
 * @author tim
 */
public final class Network implements ApplicationEventPublisherAware {
    //
    // Member data
    //
    
    @Logger
    private Log mLogger;
    
    private ApplicationEventPublisher mPublisher;
    
    private Map<UUID, NetworkEntity> mDevices;


    //
    // Construction and destruction
    //

    public Network() {
        mDevices = new HashMap<UUID, NetworkEntity>();
    }
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher iPublisher) {
        mPublisher = iPublisher;
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    @PreDestroy
    public void destroy() {
        mDevices.clear();
    }


    //
    // Basic I/O
    //
    
    public synchronized Collection<NetworkEntity> getDevices() {
        return mDevices.values();
    }
    
    public synchronized NetworkEntity getDevice(final UUID iUuid) {
        return mDevices.get(iUuid);
    }
    
    public synchronized void addDevice(final NetworkEntity iDevice) throws NetworkException{
        if (mDevices.containsKey(iDevice.getUuid())) {
            throw new NetworkException("device " + iDevice.getUuid() + " already present in network");
        }
        mDevices.put(iDevice.getUuid(), iDevice);
        
        NetworkEvent tEvent = new NetworkEvent(this, NetworkEventType.ADDED, iDevice);
        mPublisher.publishEvent(tEvent);
    }
    
    public synchronized void removeDevice(final NetworkEntity iDevice) throws NetworkException {
        if (!mDevices.containsKey(iDevice.getUuid())) {
            throw new NetworkException("device " + iDevice.getUuid() + " not present in network");
        }
        mDevices.remove(iDevice.getUuid());
        
        NetworkEvent tEvent = new NetworkEvent(this, NetworkEventType.REMOVED, iDevice);
        mPublisher.publishEvent(tEvent);
    }
}
