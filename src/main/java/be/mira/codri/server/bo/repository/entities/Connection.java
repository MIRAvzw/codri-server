/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.bo.repository.entities;

import be.mira.codri.server.bo.repository.RepositoryEntity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tim
 */
@XmlRootElement(name = "connection")
public class Connection extends RepositoryEntity {
    //
    // Member data
    //
    
    private final String mKiosk;
    private final String mConfiguration;
    private final String mPresentation;
    
    
    //
    // Construction and destruction
    //
    
    public Connection(final Long iRevision, final String iLocation, final String iKiosk, final String iConfiguration, final String iPresentation) {
        super(iRevision, iLocation);
        mKiosk = iKiosk;
        mConfiguration = iConfiguration;
        mPresentation = iPresentation;
    }
    
    
    //
    // Getters & setters
    //
    
    @XmlElement
    public final String getKiosk() {
        return mKiosk;
    }
    
    @XmlElement
    public final String getConfiguration() {
        return mConfiguration;
    }
    
    @XmlElement
    public final String getPresentation() {
        return mPresentation;
    }    
}
