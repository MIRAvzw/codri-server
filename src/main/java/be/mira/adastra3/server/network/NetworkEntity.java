/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import java.net.InetAddress;
import java.util.Date;

/**
 *
 * @author tim
 */
abstract public class NetworkEntity {
    //
    // Member data
    //
    
    private final String mVendor, mModel;
    private String mAddress;
    private Date mMark;
    
    
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
    
    public final String getVendor() {
        return mVendor;
    }
    
    public final String getModel() {
        return mModel;
    }
    
    public String getAddress() {
        return mAddress;
    }
    
    public void setAddress(String iAddress) {
        mAddress = iAddress;
    }
    
    public final Date getHeartbeat() {
        return mMark;
    }
    
    public final void updateHeartbeat() {
        mMark = new Date();
    }
    
    public final long getHeartbeatDelta() {
        return (new Date().getTime()) - mMark.getTime();
    }
}
