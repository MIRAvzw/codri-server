/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.events;

import be.mira.codri.server.bo.network.entities.Kiosk;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class NetworkKioskEvent extends NetworkEvent {
    //
    // Construction and destruction
    //
    
    public NetworkKioskEvent(final Object iSource, final NetworkEvent.NetworkEventType iType, final UUID iId, final Kiosk iKiosk) {
        super(iSource, iType, iId, iKiosk);
    }
    
    public NetworkKioskEvent(final Object iSource, final NetworkEvent.NetworkEventType iType, final UUID iId, final Kiosk iKiosk, final Kiosk iOldKiosk) {
        super(iSource, iType, iId, iKiosk, iOldKiosk);
    }
    
    
    //
    // Basic I/O
    //
    
    public final Kiosk getKiosk() {
        return (Kiosk) getEntity();
    }
}
