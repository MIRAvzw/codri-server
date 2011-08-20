/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.network.cling.CompatUpnpServiceConfiguration;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.entities.Entity;
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
    
    private Map<UUID, Entity> mDevices;
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
        // HACK
        if (System.getProperty("java.vendor").equals("Oracle Corporation")) {
            mDevices = new HashMap<UUID, Entity>();
            mUpnpService = new UpnpServiceImpl();
            mListeners = new ArrayList<INetworkListener>();            
        } else if (System.getProperty("java.vendor").equals("GNU Classpath")) {
            mDevices = new HashMap<UUID, Entity>();
            mUpnpService = new UpnpServiceImpl(new CompatUpnpServiceConfiguration());
            mListeners = new ArrayList<INetworkListener>();           
        } else {
            throw new RuntimeException("Unknown Java vendor '" + System.getProperty("java.vendor") + "'");
        }
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
    
    public Collection<Entity> getDevices() {
        return mDevices.values();
    }
    
    public Entity getDevice(final UUID iUuid) throws NetworkException {
        if (! mDevices.containsKey(iUuid) || mDevices.get(iUuid) == null) {
            throw new NetworkException("device " + iUuid + " not found in network");
        }
        return mDevices.get(iUuid);
    }
    
    public void addDevice(final Entity iDevice) throws NetworkException{
        if (mDevices.containsKey(iDevice.getUuid())) {
            throw new NetworkException("device " + iDevice.getUuid() + " already present in network");
        }
        mDevices.put(iDevice.getUuid(), iDevice);
        emitDeviceAdded(iDevice);
    }
    
    public void removeDevice(final Entity iDevice) throws NetworkException {
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
    
    private void emitDeviceAdded(final Entity iDevice) {
        for (INetworkListener tListener : mListeners) {
            tListener.doDeviceAdded(iDevice);
        }
    }
    
    private void emitDeviceRemoved(final Entity iDevice) {
        for (INetworkListener tListener : mListeners) {
            tListener.doDeviceRemoved(iDevice);
        }        
    }
}
