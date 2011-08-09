/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.INetworkListener;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.devices.Device;
import eu.webtoolkit.jwt.ItemFlag;
import eu.webtoolkit.jwt.WModelIndex;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tim
 */
public class NetworkModel extends TreeModel<NetworkItem> implements INetworkListener {
    //
    // Data members
    //
    
    private Network mNetwork = Network.getInstance();
    private SectionItem mSectionServers, mSectionKiosks;
    private Map<Device, NetworkItem> mDevices;
    
    
    //
    // Construction and destruction
    //
    
    public NetworkModel() {
        super(Arrays.asList("Device", "UUID"));
        
        // Set-up sections
        mSectionServers = getRoot().addSection("Servers");
        mSectionKiosks = getRoot().addSection("Kiosks");
        
        mDevices = new HashMap<Device, NetworkItem>();
    }
    
    public final void attach() {
        for (Device tDevice : mNetwork.getDevices()) {
            WModelIndex tIndex = getIndex(mSectionKiosks.getRow(), 0);
            NetworkItem tNetworkItem = new NetworkItem(tDevice, mSectionKiosks);                
            mDevices.put(tDevice, tNetworkItem);
            insertRows(0, Arrays.asList(tNetworkItem), tIndex);
        }
        mNetwork.addListener(this);
    }
    
    public final void detach() {
        mNetwork.removeListener(this);
    }
    
    
    //
    // Network listener
    //

    @Override
    public final void doDeviceAdded(final Device iDevice) {        
        DeferredExecution.cDeferrees.add(new DeferredExecution() {
            private Device mDevice;
            
            public DeferredExecution construct(final Device iDevice) {
                mDevice = iDevice;
                return this;
            }
            
            @Override
            public void execute() {
                WModelIndex tIndex = getIndex(mSectionKiosks.getRow(), 0);
                NetworkItem tNetworkItem = new NetworkItem(mDevice, mSectionKiosks);                
                mDevices.put(mDevice, tNetworkItem);
                insertRows(0, Arrays.asList(tNetworkItem), tIndex);
            }
        }.construct(iDevice));
    }

    @Override
    public final void doDeviceRemoved(final Device iDevice) {        
        DeferredExecution.cDeferrees.add(new DeferredExecution() {
            private Device mDevice;
            
            public DeferredExecution construct(final Device iDevice) {
                mDevice = iDevice;
                return this;
            }
            
            @Override
            public void execute() {
                WModelIndex tIndex = getIndex(mSectionKiosks.getRow(), 0);
                
                NetworkItem tNetworkItem = mDevices.remove(mDevice);      
                removeRow(tNetworkItem, tIndex);
            }
        }.construct(iDevice));
    }

    @Override
    public final void doNetworkError(final String iMessage, final NetworkException iException) {
    }

    @Override
    public final void doNetworkWarning(final String iMessage) {
    }
    
    
    //
    // Model interface
    //

    @Override
    public final EnumSet<ItemFlag> getFlags(final WModelIndex iIndex) {
        if (iIndex == null) {
            return EnumSet.noneOf(ItemFlag.class);
        } else if (getItem(iIndex) instanceof NetworkItem) {
            return EnumSet.of(ItemFlag.ItemIsSelectable);
        } else {
            return EnumSet.noneOf(ItemFlag.class);
        }
    }
}
