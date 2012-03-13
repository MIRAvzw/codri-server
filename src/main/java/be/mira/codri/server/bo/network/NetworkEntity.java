/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.network;

import be.mira.codri.server.bo.Repository;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author tim
 */
@Scope("prototype")
public abstract class NetworkEntity {
    //
    // Member data
    //
    
    private final String mVendor, mModel;
    private final int mPort;
    private String mAddress;
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
    
    
    //
    // Auxiliary
    //
    
    protected final String getEndpoint(final String[] iResources) {
        StringBuilder tEndpointBuilder = new StringBuilder("http://").append(getAddress());
        tEndpointBuilder.append(":").append(getPort());
        tEndpointBuilder.append("/");
        for (final String tResource: iResources) {
            tEndpointBuilder.append(tResource).append("/");
        }
        return tEndpointBuilder.toString();
    }
}
