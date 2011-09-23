/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.PresentationControl;
import be.mira.adastra3.server.network.controls.ConfigurationControl;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.presentation.Presentation;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class Kiosk extends NetworkEntity {
    //
    // Member data
    //
    
    private PresentationControl mPresentationControl;
    private ConfigurationControl mConfigurationControl;
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk(final UUID iUuid, final ConfigurationControl iConfigurationControl, final PresentationControl iPresentationControl) {
        super(iUuid);
        
        mConfigurationControl = iConfigurationControl;
        mPresentationControl = iPresentationControl;
    }
    
    
    //
    // Functionality
    //
    
    public final void setConfiguration(final Configuration iConfiguration) throws DeviceException {
        // Fetch the current revision
        long tCurrentRevision;
        try {
            tCurrentRevision = this.getConfigurationControl().getRevision();
        } catch(NetworkException tException) {
            throw new DeviceException("could not check the current configuration revision", tException);
        }
        
        // Push the configuration, if newer
        if (iConfiguration.getRevision() != tCurrentRevision) {
            try {
                getConfigurationControl().setVolume(iConfiguration.getSoundConfiguration().getVolume());
                getConfigurationControl().setRevision(iConfiguration.getRevision());
            } catch (NetworkException tException) {
                throw new DeviceException("could not push the configuration", tException);
            }
        }
    }
    
    public final void setPresentation(final Presentation iPresentation) throws DeviceException {
        // Fetch the current revision
        long tCurrentRevision;
        try {
            tCurrentRevision = getPresentationControl().getRevision();
        } catch(NetworkException tException) {
            throw new DeviceException("could not check the current presentation revision", tException);
        }
        
        // Push the presentation, if newer
        if (iPresentation.getRevision() != tCurrentRevision) {
            try {
                getPresentationControl().setLocation(iPresentation.getLocation());
            } catch (NetworkException tException) {
                throw new DeviceException("could not push the presentation", tException);
            }
        }
    }
    
    
    //
    // Getters and setters
    //
    
    public final PresentationControl getPresentationControl() {
        return mPresentationControl;
    }
    
    public final ConfigurationControl getConfigurationControl() {
        return mConfigurationControl;
    }
}
