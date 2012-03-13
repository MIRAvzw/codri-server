/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.network;

import be.mira.codri.server.bo.network.entities.Kiosk;
import be.mira.codri.server.bo.Network;
import be.mira.codri.server.exceptions.NetworkException;
import be.mira.codri.server.spring.Slf4jLogger;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author tim
 */
public class ExpiryChecker extends TimerTask {
    //
    // Data members
    //
    
    @Slf4jLogger
    private Logger mLogger;
    
    private Network mNetwork;

    private @Value("${network.expiry.threshold}") Long mExpiryThreshold;
    
    
    //
    // Construction and destruction
    //    
    
    @Required
    @Autowired
    public void setNetwork(Network iNetwork) {
        mNetwork = iNetwork;
    }
    
    
    //
    // TimerTask implementation
    //
    
    @Override
    public void run() {
        mLogger.trace("Checking for expired network entities");
        
        // Check all kiosks
        for (Map.Entry<UUID, Kiosk> tEntry: mNetwork.getKiosks().entrySet()) {
            if (tEntry.getValue().getHeartbeatDelta() > mExpiryThreshold) {
                try {
                    mLogger.debug("Trying to expire kiosk {}", tEntry.getKey());
                    mNetwork.expireKiosk(tEntry.getKey());
                } catch (NetworkException tException) {
                    mLogger.error("Could not expire kiosk {}", tEntry.getKey(), tException);
                }
            }
        }
    }    
}
