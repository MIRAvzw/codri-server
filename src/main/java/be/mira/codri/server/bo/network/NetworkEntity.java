/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.bo.network;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author tim
 */
public abstract class NetworkEntity {
    //
    // Member data
    //
    
    private final String mVendor, mModel;
    private final int mPort;
    private Date mHeartbeat;
    
    
    //
    // Construction and destruction
    //
    
    public NetworkEntity(final String iVendor, final String iModel, final int iPort) {
        mVendor = iVendor;
        mModel = iModel;
        mPort = iPort;
        updateHeartbeat();
    }


    //
    // Basic I/O
    //
    
    @XmlElement
    public final String getVendor() {
        return mVendor;
    }
    
    @XmlElement
    public final String getModel() {
        return mModel;
    }
    
    @XmlElement
    public final int getPort() {
        return mPort;
    }
    
    @XmlElement
    public final Date getHeartbeat() {
        return mHeartbeat;
    }
    
    public final void updateHeartbeat() {
        mHeartbeat = new Date();
    }
    
    @XmlElement
    public final long getHeartbeatDelta() {
        return (new Date().getTime()) - mHeartbeat.getTime();
    }
}
