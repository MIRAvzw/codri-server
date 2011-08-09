/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import java.util.UUID;

/**
 *
 * @author tim
 */
public class KioskConfiguration extends Configuration {
    //
    // Data members
    //
    
    private DeviceConfiguration mDeviceConfiguration;
    private ApplicationConfiguration mApplicationConfiguration;
    private String mId;
    
    
    //
    // Construction and destruction
    //
    
    public KioskConfiguration(final String iId) {
        mId = iId;
        
        setDeviceConfiguration(new DeviceConfiguration());
        setApplicationConfiguration(new ApplicationConfiguration());
    }
    
    
    //
    // Getters and setters
    //

    public final String getId() {
        return mId;
    }

    public final UUID getTarget() {
        if (getProperty("target") != null) {
            return (UUID) getProperty("target");
        } else {
            return null;
        }
    }

    public final void setTarget(final UUID iTarget) {
        setProperty("target", iTarget);
    }

    public final ApplicationConfiguration getApplicationConfiguration() {
        return mApplicationConfiguration;
    }

    public final void setApplicationConfiguration(final ApplicationConfiguration iApplicationConfiguration) {
        if (iApplicationConfiguration == null) {
            return;
        }
        mApplicationConfiguration = iApplicationConfiguration;
    }

    public final DeviceConfiguration getDeviceConfiguration() {
        return mDeviceConfiguration;
    }

    public final void setDeviceConfiguration(final DeviceConfiguration iDeviceConfiguration) {
        if (iDeviceConfiguration == null) {
            return;
        }
        mDeviceConfiguration = iDeviceConfiguration;
    }
}
