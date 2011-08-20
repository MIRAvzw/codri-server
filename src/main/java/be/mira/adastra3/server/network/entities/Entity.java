/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.entities;

import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.repository.configurations.Configuration;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author tim
 */
abstract public class Entity {
    //
    // Member data
    //
    
    private UUID mUuid;
    private String mName;
    private Date mMark;
    
    
    //
    // Construction and destruction
    //
    
    public Entity(final UUID iUuid) {
        mUuid = iUuid;
        setMark();
    }
    
    
    //
    // Configuration handling
    //
    
    public abstract void setConfiguration(Configuration iConfiguration) throws DeviceException;
    
    
    //
    // Getters and setters
    //
    
    public final UUID getUuid() {
        return mUuid;
    }
    
    public final String getName() {
        return mName;
    }
    
    public final void setName(final String iName) {
        mName = iName;
    }
    
    public final void setMark() {
        mMark = new Date();
    }
    
    public final Date getMark() {
        return mMark;
    }
    
    public final long getMarkDelta() {
        return (new Date().getTime()) - mMark.getTime();
    }
}