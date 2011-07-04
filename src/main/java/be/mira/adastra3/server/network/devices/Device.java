/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.devices;

import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.repository.configurations.Configuration;
import java.util.UUID;

/**
 *
 * @author tim
 */
abstract public class Device {
    //
    // Member data
    //
    
    private UUID mUuid;
    private String mName;
    
    
    //
    // Construction and destruction
    //
    
    public Device(UUID iUuid) {
        mUuid = iUuid;
    }
    
    
    //
    // Configuration handling
    //
    
    abstract public void setConfiguration(Configuration iConfiguration) throws DeviceException;
    
    
    //
    // Getters and setters
    //
    
    public UUID getUuid() {
        return mUuid;
    }
    
    public String getName() {
        return mName;
    }
    
    public void setName(String iName) {
        mName = iName;
    }
}
