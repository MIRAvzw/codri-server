/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.DeviceControl;
import be.mira.adastra3.server.network.controls.ApplicationControl;
import java.util.HashMap;
import java.util.Map;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.ControlPoint;
import org.teleal.cling.model.types.UDN;

/**
 *
 * @author tim
 */
public class Network {
    //
    // Member data
    //
    
    private Map<UDN, DeviceControl> mKioskControls;
    private Map<UDN, ApplicationControl> mMediaControls;
    private UpnpService mUpnpService;


    //
    // Static functionality
    //

    private static Network mInstance;

    public static Network getInstance() {
        if (mInstance == null)
            mInstance = new Network();
        return mInstance;
    }


    //
    // Construction and destruction
    //

    private Network() {
        mKioskControls = new HashMap<UDN, DeviceControl>();
        mMediaControls = new HashMap<UDN, ApplicationControl>();
        mUpnpService = new UpnpServiceImpl();
    }


    //
    // Getters and setters
    //
    
    public static ControlPoint getControlPoint() {
        return Network.getInstance().getUpnpService().getControlPoint();
    }
    
    public UpnpService getUpnpService() {
        return mUpnpService;
    }
    
    public DeviceControl getKioskControl(UDN iUDN) {
        return mKioskControls.get(iUDN);
    }
    
    public void addKioskControl(UDN iUDN, DeviceControl iKioskControl) throws NetworkException{
        if (mKioskControls.containsKey(iUDN))
            throw new NetworkException("device " + iUDN + " already present in network");
        mKioskControls.put(iUDN, iKioskControl);
    }
    
    public DeviceControl removeKioskControl(UDN iUDN) throws NetworkException {
        if (!mKioskControls.containsKey(iUDN))
            throw new NetworkException("device " + iUDN + " not present in network");
        return mKioskControls.remove(iUDN);
    }
    
    public ApplicationControl getMediaControl(UDN iUDN) {
        return mMediaControls.get(iUDN);
    }
    
    public void addMediaControl(UDN iUDN, ApplicationControl iMediaControl) throws NetworkException {
        if (mMediaControls.containsKey(iUDN))
            throw new NetworkException("device " + iUDN + " already present in network");
        mMediaControls.put(iUDN, iMediaControl);
    }
    
    public ApplicationControl removeMediaControl(UDN iUDN) throws NetworkException {
        if (!mMediaControls.containsKey(iUDN))
            throw new NetworkException("device " + iUDN + " not present in network");
        return mMediaControls.remove(iUDN);
    }
    
}
