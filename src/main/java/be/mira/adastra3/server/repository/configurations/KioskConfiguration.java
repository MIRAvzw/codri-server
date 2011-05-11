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
    
    
    //
    // Construction and destruction
    //
    
    public KioskConfiguration(String iName, Configuration iParent) {
        setProperty("name", iName);
        
        setParent(iParent);
        
        setDeviceConfiguration(new DeviceConfiguration());
        setApplicationConfiguration(new ApplicationConfiguration());
    }
    
    
    //
    // Getters and setters
    //

    public boolean isAbstract() {
        return (getTarget() == null);
    }

    public String getName() {
        if (getProperty("name") != null)
            return (String) getProperty("name");
        else
            return null;
    }

    public UUID getTarget() {
        if (getProperty("UUID") != null)
            return (UUID) getProperty("UUID");
        else
            return null;
    }

    public void setTarget(UUID iTarget) {
        setProperty("target", iTarget);
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return mApplicationConfiguration;
    }

    public final void setApplicationConfiguration(ApplicationConfiguration iApplicationConfiguration) {
        if (iApplicationConfiguration == null)
            return;
        mApplicationConfiguration = iApplicationConfiguration;
        if (getParent() != null)
            mApplicationConfiguration.setParent(((KioskConfiguration)getParent()).mApplicationConfiguration);
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return mDeviceConfiguration;
    }

    public final void setDeviceConfiguration(DeviceConfiguration iDeviceConfiguration) {
        if (iDeviceConfiguration == null)
            return;
        mDeviceConfiguration = iDeviceConfiguration;
        if (getParent() != null)
            mDeviceConfiguration.setParent(((KioskConfiguration)getParent()).mDeviceConfiguration);
    }
}
