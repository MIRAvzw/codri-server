/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.DeviceControl;
import be.mira.adastra3.server.network.controls.ApplicationControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.ControlPoint;

/**
 *
 * @author tim
 */
public class Network {
    //
    // Member data
    //
    
    private Map<UUID, DeviceControl> mDeviceControls;
    private Map<UUID, ApplicationControl> mApplicationControls;
    private UpnpService mUpnpService;
    private List<INetworkListener> mListeners;


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
        mDeviceControls = new HashMap<UUID, DeviceControl>();
        mApplicationControls = new HashMap<UUID, ApplicationControl>();
        mUpnpService = new UpnpServiceImpl();
        mListeners = new ArrayList<INetworkListener>();
    }


    //
    // Getters and setters
    //
    
    public void addListener(INetworkListener iListener) {
        mListeners.add(iListener);
    }
    
    public void removeListener(INetworkListener iListener) {
        mListeners.remove(iListener);
    }
    
    public static ControlPoint getControlPoint() {
        return Network.getInstance().getUpnpService().getControlPoint();
    }
    
    public UpnpService getUpnpService() {
        return mUpnpService;
    }
    
    public DeviceControl getDeviceControl(UUID iUuid) {
        return mDeviceControls.get(iUuid);
    }
    
    public void addDeviceControl(UUID iUuid, DeviceControl iDeviceControl) throws NetworkException{
        if (mDeviceControls.containsKey(iUuid))
            throw new NetworkException("device " + iUuid + " already present in network");
        emitDeviceControlAdded(iUuid, iDeviceControl);
        mDeviceControls.put(iUuid, iDeviceControl);
    }
    
    public DeviceControl removeDeviceControl(UUID iUuid) throws NetworkException {
        if (!mDeviceControls.containsKey(iUuid))
            throw new NetworkException("device " + iUuid + " not present in network");
        emitDeviceControlRemoved(iUuid);
        return mDeviceControls.remove(iUuid);
    }
    
    public ApplicationControl getApplicationControl(UUID iUuid) {
        return mApplicationControls.get(iUuid);
    }
    
    public void addApplicationControl(UUID iUuid, ApplicationControl iApplicationControl) throws NetworkException {
        if (mApplicationControls.containsKey(iUuid))
            throw new NetworkException("device " + iUuid + " already present in network");
        emitApplicationControlAdded(iUuid, iApplicationControl);
        mApplicationControls.put(iUuid, iApplicationControl);
    }
    
    public ApplicationControl removeApplicationControl(UUID iUuid) throws NetworkException {
        if (!mApplicationControls.containsKey(iUuid))
            throw new NetworkException("device " + iUuid + " not present in network");
        emitApplicationControlRemoved(iUuid);
        return mApplicationControls.remove(iUuid);
    }
    
    
    //
    // Signals
    //
    
    public void emitError(String iMessage, NetworkException iException) {
        for (INetworkListener tListener : mListeners) {
            tListener.doNetworkError(iMessage, iException);
        }
    }
    
    public void emitWarning(String iMessage) {
        for (INetworkListener tListener : mListeners) {
            tListener.doNetworkWarning(iMessage);
        }
    }
    
    private void emitDeviceControlAdded(UUID iUuid, DeviceControl iMediaControl) {
        for (INetworkListener tListener : mListeners) {
            tListener.doDeviceControlAdded(iUuid, iMediaControl);
        }
    }
    
    private void emitDeviceControlRemoved(UUID iUuid) {
        for (INetworkListener tListener : mListeners) {
            tListener.doDeviceControlRemoved(iUuid);
        }
    }
    
    private void emitApplicationControlAdded(UUID iUuid, ApplicationControl iApplicationControl) {
        for (INetworkListener tListener : mListeners) {
            tListener.doApplicationControlAdded(iUuid, iApplicationControl);
        }
    }
    
    private void emitApplicationControlRemoved(UUID iUuid) {
        for (INetworkListener tListener : mListeners) {
            tListener.doApplicationControlRemoved(iUuid);
        }
    }
}
