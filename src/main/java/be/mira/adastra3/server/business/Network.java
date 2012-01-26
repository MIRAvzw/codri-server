/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.business;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.spring.Logger;
import be.mira.adastra3.server.events.NetworkEvent;
import be.mira.adastra3.server.events.NetworkEvent.NetworkEventType;
import be.mira.adastra3.server.events.NetworkKioskEvent;
import be.mira.adastra3.server.network.Kiosk;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * 
 * @author tim
 */
@XmlRootElement(name="network")
public final class Network implements ApplicationEventPublisherAware {
    //
    // Member data
    //
    
    @Logger
    private Log mLogger;
    
    private ApplicationEventPublisher mPublisher;
    
    private final Map<UUID, Kiosk> mKiosks;


    //
    // Construction and destruction
    //

    public Network() {
        mKiosks = new HashMap<UUID, Kiosk>();
    }
    
    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher iPublisher) {
        mPublisher = iPublisher;
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    @PreDestroy
    public void destroy() {
        mKiosks.clear();
    }


    //
    // Basic I/O
    //
    
    @XmlElementWrapper(name="devices")
    @XmlElement(name="device")
    public Map<UUID, Kiosk> getKiosks() {
        return mKiosks;
    }
    
    public Kiosk getKiosk(final UUID iUuid) {
        synchronized (mKiosks) {
            return mKiosks.get(iUuid);
        }
    }
    
    public void addKiosk(final UUID iId, final Kiosk iKiosk) throws NetworkException {
        synchronized (mKiosks) {
            if (mKiosks.containsKey(iId)) {
                throw new NetworkException("kiosk " + iId + " already present in network");
            }
            mKiosks.put(iId, iKiosk);            
        }
        
        NetworkEvent tEvent = new NetworkKioskEvent(this, NetworkEventType.ADDED, iId, iKiosk);
        mPublisher.publishEvent(tEvent);
    }
    
    public void removeKiosk(final UUID iId, final Kiosk iKiosk) throws NetworkException {
        synchronized (mKiosks) {
            if (!mKiosks.containsKey(iId)) {
                throw new NetworkException("kiosk " + iId + " not present in network");
            }
            mKiosks.remove(iId);
        }
        
        NetworkEvent tEvent = new NetworkKioskEvent(this, NetworkEventType.REMOVED, iId, iKiosk);
        mPublisher.publishEvent(tEvent);
    }
}
