/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.network.entities;

import be.mira.codri.server.bo.network.NetworkEntity;
import be.mira.codri.server.exceptions.DeviceException;
import be.mira.codri.server.bo.repository.entities.Configuration;
import be.mira.codri.server.bo.repository.entities.Presentation;
import be.mira.codri.server.spring.Slf4jLogger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author tim
 */
@XmlRootElement(name = "kiosk")
public class Kiosk extends NetworkEntity {
    //
    // Member data
    //
    
    @Slf4jLogger
    private Logger mLogger;
    
    private RestTemplate mRestTemplate;
    
    
    //
    // Construction and destruction
    //
    
    @JsonCreator
    public Kiosk(@JsonProperty("vendor") final String iVendor, @JsonProperty("model") final String iModel, @JsonProperty("port") final int iPort) {
        super(iVendor, iModel, iPort);
        mRestTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> tList = new ArrayList<HttpMessageConverter<?>>();
        tList.add(new MappingJacksonHttpMessageConverter());
        mRestTemplate.setMessageConverters(tList);
    }
    
    
    //
    // Functionality
    //
    
    public final void setConfiguration(final Configuration iConfiguration) throws DeviceException {
        String tEndpoint = getEndpoint(new String[]{"configuration"});
        mLogger.debug("Setting a configuration on endpoint {}", tEndpoint);
        
        mRestTemplate.put(tEndpoint, iConfiguration);
    }
    
    public final void setPresentation(final Presentation iPresentation) throws DeviceException {
        String tEndpoint = getEndpoint(new String[]{"presentation"});
        mLogger.debug("Setting a presentation on endpoint {}", tEndpoint);
        
        mRestTemplate.put(tEndpoint, iPresentation);
    }
}
