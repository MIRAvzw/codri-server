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
    private final List<INetworkListener> mListeners;


    //
    // Static functionality
    //

    private static Network INSTANCE;

    public static synchronized Network getInstance() {
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
    
    public void reset() {
        mDevices.clear();
        mListeners.clear();
    }


    //
    // Getters and setters
    //
    
    public void addListener(final INetworkListener iListener) {
        synchronized(mListeners) {
            mListeners.add(iListener);
        }
    }
    
    public void removeListener(final INetworkListener iListener) {
        synchronized(mListeners) {
            mListeners.remove(iListener);
        }
    }
    
    public static ControlPoint getControlPoint() {
        return Network.getInstance().getUpnpService().getControlPoint();
    }
    
    public synchronized UpnpService getUpnpService() {
        return mUpnpService;
    }
    
    public synchronized Collection<NetworkEntity> getDevices() {
        return mDevices.values();
    }
    
    public synchronized NetworkEntity getDevice(final UUID iUuid) {
        return mDevices.get(iUuid);
    }
    
    public synchronized void addDevice(final NetworkEntity iDevice) throws NetworkException{
        if (mDevices.containsKey(iDevice.getUuid())) {
            throw new NetworkException("device " + iDevice.getUuid() + " already present in network");
        }
        mDevices.put(iDevice.getUuid(), iDevice);
        emitDeviceAdded(iDevice);
    }
    
    public synchronized void removeDevice(final NetworkEntity iDevice) throws NetworkException {
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
        synchronized(mListeners) {
            for (INetworkListener tListener : mListeners) {
                tListener.doNetworkError(iMessage, iException);
            }
        }
    }
    
    public void emitWarning(final String iMessage) {
        synchronized(mListeners) {
                for (INetworkListener tListener : mListeners) {
                tListener.doNetworkWarning(iMessage);
            }
        }
    }
    
    private void emitDeviceAdded(final NetworkEntity iDevice) {
        synchronized(mListeners) {
                for (INetworkListener tListener : mListeners) {
                tListener.doEntityAdded(iDevice);
            }
        }
    }
    
    private void emitDeviceRemoved(final NetworkEntity iDevice) {
        synchronized(mListeners) {
                for (INetworkListener tListener : mListeners) {
                tListener.doEntityRemoved(iDevice);
            }
        }
    }
}
