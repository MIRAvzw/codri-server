/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo;

import be.mira.codri.server.exceptions.NetworkException;
import be.mira.codri.server.events.NetworkEvent;
import be.mira.codri.server.events.NetworkEvent.NetworkEventType;
import be.mira.codri.server.events.NetworkKioskEvent;
import be.mira.codri.server.bo.network.Kiosk;
import be.mira.codri.server.spring.Slf4jLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
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
    
    @Slf4jLogger
    private Logger mLogger;
    
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
    
    @PreDestroy
    public void destroy() {
        mKiosks.clear();
    }


    //
    // Basic I/O
    //
    
    @XmlElementWrapper(name="kiosks")
    @XmlElement(name="kiosk")
    public Map<UUID, Kiosk> getKiosks() {
        return mKiosks;
    }
    
    public Kiosk getKiosk(final UUID iUuid) {
        synchronized (mKiosks) {
            return mKiosks.get(iUuid);
        }
    }
    
    public void addKiosk(final UUID iId, final Kiosk iKiosk) throws NetworkException {
        mLogger.info("Adding kiosk {}", iId);
        
        synchronized (mKiosks) {
            if (mKiosks.containsKey(iId)) {
                throw new NetworkException("kiosk " + iId + " already is present in network");
            }
            mKiosks.put(iId, iKiosk);            
        }
        
        NetworkEvent tEvent = new NetworkKioskEvent(this, NetworkEventType.ADDED, iId, iKiosk);
        mPublisher.publishEvent(tEvent);
    }
    
    public void removeKiosk(final UUID iId, final Kiosk iKiosk) throws NetworkException {
        mLogger.info("Removing kiosk {}", iId);
        
        synchronized (mKiosks) {
            if (!mKiosks.containsKey(iId)) {
                throw new NetworkException("kiosk " + iId + " is not present in network");
            }
            mKiosks.remove(iId);
        }
        
        NetworkEvent tEvent = new NetworkKioskEvent(this, NetworkEventType.REMOVED, iId, iKiosk);
        mPublisher.publishEvent(tEvent);
    }
}
