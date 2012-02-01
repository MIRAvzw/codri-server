/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
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
    private String mAddress;
    private int mPort;
    private Date mHeartbeat;
    
    
    //
    // Construction and destruction
    //
    
    public NetworkEntity(final String iVendor, final String iModel) {
        mVendor = iVendor;
        mModel = iModel;
        updateHeartbeat();
    }
    
    
    //
    // Getters and setters
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
    public final String getAddress() {
        return mAddress;
    }
    
    public final void setAddress(final String iAddress) {
        mAddress = iAddress;
    }
    
    @XmlElement
    public final int getPort() {
        return mPort;
    }
    
    public final void setPort(final int iPort) {
        mPort = iPort;
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
