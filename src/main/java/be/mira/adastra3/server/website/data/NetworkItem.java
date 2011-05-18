/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import be.mira.adastra3.server.network.devices.Device;

/**
 *
 * @author tim
 */
public class NetworkItem extends TreeItem {
    //
    // Data members
    //
    
    private Device mDevice;
    
    
    //
    // Construction and destruction
    //

    public NetworkItem(Device iDevice, TreeItem iParent) {
        super(iParent);
        mDevice = iDevice;
    }
    
    
    //
    // Item implementation
    //

    @Override
    public Object getField(int iField) {
        switch (iField) {
            case 1:
                return mDevice.getUuid();
            default:
                return null;
        }
    }

    @Override
    public int getFieldCount() {
        return 1;
    }    
}
