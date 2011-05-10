/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.exceptions.RepositoryException;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class KioskConfiguration implements IConfiguration {
    //
    // Data members
    //
    
    private UUID mTarget;
    private Boolean mAbstract;
    private String mName;
    private DeviceConfiguration mDeviceConfiguration;
    private ApplicationConfiguration mApplicationConfiguration;
    
    
    //
    // Getters and setters
    //
    
    @Override
    public void check() throws RepositoryException {
        if (isAbstract() && getTarget() != null)
            throw new RepositoryException("Abstract kiosk cannot have an instance target");
        if (!isAbstract() && getTarget() == null)
            throw new RepositoryException("Non-abstract kiosk should have an instance target");
    }

    public boolean isAbstract() {
        if (mAbstract == null)
            return false;
        return mAbstract;
    }

    public void setAbstract(boolean iAbstract) {
        mAbstract = iAbstract;
    }

    public String getName() {
        return mName;
    }

    public void setName(String iName) {
        mName = iName;
    }

    public UUID getTarget() {
        return mTarget;
    }

    public void setTarget(UUID iTarget) {
        mTarget = iTarget;
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return mApplicationConfiguration;
    }

    public void setApplicationConfiguration(ApplicationConfiguration iApplicationConfiguration) {
        mApplicationConfiguration = iApplicationConfiguration;
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return mDeviceConfiguration;
    }

    public void setDeviceConfiguration(DeviceConfiguration iDeviceConfiguration) {
        mDeviceConfiguration = iDeviceConfiguration;
    }
}
