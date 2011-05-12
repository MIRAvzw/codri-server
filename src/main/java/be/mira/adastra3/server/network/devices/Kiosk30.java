/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.devices;

import be.mira.adastra3.server.network.controls.ApplicationControl;
import be.mira.adastra3.server.network.controls.DeviceControl;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class Kiosk30 extends Device {
    //
    // Member data
    //
    
    private ApplicationControl mApplicationControl;
    private DeviceControl mDeviceControl;
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk30(UUID iUuid, DeviceControl iDeviceControle, ApplicationControl iApplicationControl) {
        super(iUuid);
        
        mDeviceControl = iDeviceControle;
        mApplicationControl = iApplicationControl;
    }
    
    
    //
    // Getters and setters
    //
    
    public ApplicationControl getApplicationControl() {
        return mApplicationControl;
    }
    
    public DeviceControl getDeviceControl() {
        return mDeviceControl;
    }
}
