/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.devices;

import java.util.UUID;

/**
 *
 * @author tim
 */
public class Device {
    //
    // Member data
    //
    
    private UUID mUuid;
    
    
    //
    // Construction and destruction
    //
    
    public Device(UUID iUuid) {
        mUuid = iUuid;
    }
    
    
    //
    // Getters and setters
    //
    
    public UUID getUuid() {
        return mUuid;
    }
}
