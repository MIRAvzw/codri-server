/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.repository.configurations.objects.ApplicationConfiguration;
import be.mira.adastra3.server.repository.configurations.objects.DeviceConfiguration;
import java.util.UUID;

/**
 *
 * @author tim
 */
public final class Kiosk30Configuration extends Configuration {
    //
    // Data members
    //
    
    private DeviceConfiguration mDevice;
    private ApplicationConfiguration mApplication;
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk30Configuration(final String iId, final UUID iTarget, final DeviceConfiguration iDevice, final ApplicationConfiguration iApplication) {
        super(iId, iTarget);
        mDevice = iDevice;
        mApplication = iApplication;
    }
    
    
    //
    // Getters
    //
    
    public DeviceConfiguration getDevice() {
        return mDevice;
    }
    
    public ApplicationConfiguration getApplication() {
        return mApplication;
    }
}
