/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.presentation.Presentation;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

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
    
    @JsonCreator
    public Kiosk(final @JsonProperty("vendor") String iVendor, final @JsonProperty("model") String iModel) {
        super(iVendor, iModel);
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
