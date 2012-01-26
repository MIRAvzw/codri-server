/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.events;

import be.mira.adastra3.server.network.Kiosk;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class NetworkKioskEvent extends NetworkEvent {
    //
    // Construction and destruction
    //
    
    public NetworkKioskEvent(Object iSource, NetworkEvent.NetworkEventType iType, UUID iId, Kiosk iKiosk) {
        super(iSource, iType, iId, iKiosk);
    }
    
    public NetworkKioskEvent(Object iSource, NetworkEvent.NetworkEventType iType, UUID iId, Kiosk iKiosk, Kiosk iOldKiosk) {
        super(iSource, iType, iId, iKiosk, iOldKiosk);
    }
}
