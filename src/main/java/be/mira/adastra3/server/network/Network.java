/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.devices.Device;
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
    
    private Map<UUID, Device> mDevices;
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
        mDevices = new HashMap<UUID, Device>();
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
    
    public Device getDevice(UUID iUuid) {
        return mDevices.get(iUuid);
    }
    
    public void addDevice(Device iDevice) throws NetworkException{
        if (mDevices.containsKey(iDevice.getUuid()))
            throw new NetworkException("device " + iDevice.getUuid() + " already present in network");
        mDevices.put(iDevice.getUuid(), iDevice);
        emitDeviceAdded(iDevice);
    }
    
    public void removeDevice(Device iDevice) throws NetworkException {
        if (!mDevices.containsKey(iDevice.getUuid()))
            throw new NetworkException("device " + iDevice.getUuid() + " not present in network");
        mDevices.remove(iDevice.getUuid());
        emitDeviceRemoved(iDevice);
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
    
    private void emitDeviceAdded(Device iDevice) {
        for (INetworkListener tListener : mListeners) {
            tListener.doDeviceAdded(iDevice);
        }
    }
    
    private void emitDeviceRemoved(Device iDevice) {
        for (INetworkListener tListener : mListeners) {
            tListener.doDeviceRemoved(iDevice);
        }        
    }
}
