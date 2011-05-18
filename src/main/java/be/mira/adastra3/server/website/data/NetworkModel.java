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
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WModelIndex;
import java.util.Arrays;
import java.util.EnumSet;

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
    
    
    //
    // Construction and destruction
    //
    
    public NetworkModel() {
        super(Arrays.asList("Device", "UUID"));
        
        // Set-up sections
        mSectionServers = getRoot().addSection("Servers");
        mSectionKiosks = getRoot().addSection("Kiosks");
    }
    
    public void attach() {
        mNetwork.addListener(this);
    }
    
    public void detach() {
        // TODO: this doesn't get called (no Finalize @ application level?)
        mNetwork.removeListener(this);
    }
    
    
    //
    // Network listener
    //

    @Override
    public void doDeviceAdded(Device iDevice) {
        System.err.println("Adding device");
        
        WModelIndex tIndex = getIndex(mSectionKiosks.getRow(), 0);
        this.insertRows(0, Arrays.asList(new NetworkItem(iDevice, mSectionKiosks)), tIndex);
    }

    @Override
    public void doDeviceRemoved(Device iDevice) {
    }

    @Override
    public void doNetworkError(String iMessage, NetworkException iException) {
    }

    @Override
    public void doNetworkWarning(String iMessage) {
    }
    
    
    //
    // Model interface
    //

    @Override
    public EnumSet<ItemFlag> getFlags(WModelIndex index) {
        if (index == null)
            return EnumSet.noneOf(ItemFlag.class);
        return EnumSet.of(ItemFlag.ItemIsSelectable);
    }
    
    
}
