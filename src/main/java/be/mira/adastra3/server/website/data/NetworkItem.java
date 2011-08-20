/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import be.mira.adastra3.server.network.entities.Entity;

/**
 *
 * @author tim
 */
public class NetworkItem extends TreeItem {
    //
    // Data members
    //
    
    private Entity mDevice;
    
    
    //
    // Construction and destruction
    //

    public NetworkItem(final Entity iDevice, final TreeItem iParent) {
        super(iParent);
        mDevice = iDevice;
    }
    
    
    //
    // Item implementation
    //

    @Override
    public final Object getField(final int iField) {
        switch (iField) {
            case 0:
                return mDevice.getName();
            case 1:
                return mDevice.getUuid();
            default:
                return null;
        }
    }

    @Override
    public final int getFieldCount() {
        return 2;
    }
    
    
    //
    // Basic I/O
    //
    
    public final Entity getDevice() {
        return mDevice;
    }
}
