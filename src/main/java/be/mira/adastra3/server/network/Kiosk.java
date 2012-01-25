/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.DeviceException;
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
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk(final UUID iUuid) {
        super(iUuid);
    }
    
    
    //
    // Functionality
    //
    
    public final void setConfiguration(final Configuration iConfiguration) throws DeviceException {
        throw new UnsupportedOperationException();
    }
    
    public final void setPresentation(final Presentation iPresentation) throws DeviceException {
        throw new UnsupportedOperationException();
    }
}
