/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;
import java.util.ArrayList;
import java.util.Collection;
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
public final class Network {
    //
    // Member data
    //
    
    private Map<UUID, NetworkEntity> mDevices;
    private UpnpService mUpnpService;
    private List<INetworkListener> mListeners;


    //
    // Static functionality
    //

    private static Network INSTANCE;

    public static Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();
        }
        return INSTANCE;
    }


    //
    // Construction and destruction
    //

    private Network() {
        mDevices = new HashMap<UUID, NetworkEntity>();
        mUpnpService = new UpnpServiceImpl();
        mListeners = new ArrayList<INetworkListener>();
    }


    //
    // Getters and setters
    //
    
    public void addListener(final INetworkListener iListener) {
        mListeners.add(iListener);
    }
    
    public void removeListener(final INetworkListener iListener) {
        mListeners.remove(iListener);
    }
    
    public static ControlPoint getControlPoint() {
        return Network.getInstance().getUpnpService().getControlPoint();
    }
    
    public UpnpService getUpnpService() {
        return mUpnpService;
    }
    
    public Collection<NetworkEntity> getDevices() {
        return mDevices.values();
    }
    
    public NetworkEntity getDevice(final UUID iUuid) {
        return mDevices.get(iUuid);
    }
    
    public void addDevice(final NetworkEntity iDevice) throws NetworkException{
        if (mDevices.containsKey(iDevice.getUuid())) {
            throw new NetworkException("device " + iDevice.getUuid() + " already present in network");
        }
        mDevices.put(iDevice.getUuid(), iDevice);
        emitDeviceAdded(iDevice);
    }
    
    public void removeDevice(final NetworkEntity iDevice) throws NetworkException {
        if (!mDevices.containsKey(iDevice.getUuid())) {
            throw new NetworkException("device " + iDevice.getUuid() + " not present in network");
        }
        mDevices.remove(iDevice.getUuid());
        emitDeviceRemoved(iDevice);
    }
    
    
    //
    // Signals
    //
    
    public void emitError(final String iMessage, final NetworkException iException) {
        for (INetworkListener tListener : mListeners) {
            tListener.doNetworkError(iMessage, iException);
        }
    }
    
    public void emitWarning(final String iMessage) {
        for (INetworkListener tListener : mListeners) {
            tListener.doNetworkWarning(iMessage);
        }
    }
    
    private void emitDeviceAdded(final NetworkEntity iDevice) {
        for (INetworkListener tListener : mListeners) {
            tListener.doEntityAdded(iDevice);
        }
    }
    
    private void emitDeviceRemoved(final NetworkEntity iDevice) {
        for (INetworkListener tListener : mListeners) {
            tListener.doEntityRemoved(iDevice);
        }        
    }
}
