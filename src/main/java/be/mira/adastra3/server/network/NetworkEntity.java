/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

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
    
    public final void updateHeartbeat() {
        mMark = new Date();
    }
    
    public final Date getHeartbeat() {
        return mMark;
    }
    
    public final long getHeartbeatDelta() {
        return (new Date().getTime()) - mMark.getTime();
    }
}
