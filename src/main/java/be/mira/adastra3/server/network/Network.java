/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.KioskControl;
import be.mira.adastra3.server.network.controls.MediaControl;
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
    
    private Map<UDN, KioskControl> mKioskControls;
    private Map<UDN, MediaControl> mMediaControls;
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
        mKioskControls = new HashMap<UDN, KioskControl>();
        mMediaControls = new HashMap<UDN, MediaControl>();
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
    
    public KioskControl getKioskControl(UDN iUDN) {
        return mKioskControls.get(iUDN);
    }
    
    public void addKioskControl(UDN iUDN, KioskControl iKioskControl) throws NetworkException{
        if (mKioskControls.containsKey(iUDN))
            throw new NetworkException("Cannot add kiosk control: device " + iUDN + " already present in network");
        mKioskControls.put(iUDN, iKioskControl);
    }
    
    public KioskControl removeKioskControl(UDN iUDN) throws NetworkException {
        if (!mKioskControls.containsKey(iUDN))
            throw new NetworkException("Cannot remove kiosk control: device " + iUDN + " not present in network");
        return mKioskControls.remove(iUDN);
    }
    
    public MediaControl getMediaControl(UDN iUDN) {
        return mMediaControls.get(iUDN);
    }
    
    public void addMediaControl(UDN iUDN, MediaControl iMediaControl) throws NetworkException {
        if (mMediaControls.containsKey(iUDN))
            throw new NetworkException("Cannot add media control: device " + iUDN + " already present in network");
        mMediaControls.put(iUDN, iMediaControl);
    }
    
    public MediaControl removeMediaControl(UDN iUDN) throws NetworkException {
        if (!mMediaControls.containsKey(iUDN))
            throw new NetworkException("Cannot remove media control: device " + iUDN + " not present in network");
        return mMediaControls.remove(iUDN);
    }
    
}
