/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.network;

import be.mira.codri.server.exceptions.DeviceException;
import be.mira.codri.server.bo.repository.entities.Configuration;
import be.mira.codri.server.bo.repository.entities.Presentation;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author tim
 */
@XmlRootElement(name="kiosk")
public class Kiosk extends NetworkEntity {
    //
    // Member data
    //
    
    
    //
    // Construction and destruction
    //
    
    @JsonCreator
    public Kiosk(final @JsonProperty("vendor") String iVendor, final @JsonProperty("model") String iModel, final @JsonProperty("port") int iPort) {
        super(iVendor, iModel, iPort);
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
