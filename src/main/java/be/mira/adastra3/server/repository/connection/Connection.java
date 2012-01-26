/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.repository.connection;

import be.mira.adastra3.server.repository.RepositoryEntity;
import java.util.UUID;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tim
 */
@XmlRootElement(name="connection")
public class Connection extends RepositoryEntity {
    //
    // Member data
    //
    
    private UUID mKiosk;
    private String mConfiguration;
    private String mPresentation;
    
    
    //
    // Construction and destruction
    //
    
    public Connection(final Long iRevision, final String iPath, final String iServer, final UUID iKiosk, final String iConfiguration, final String iPresentation) {
        super(iRevision, iPath, iServer);
        mKiosk = iKiosk;
        mConfiguration = iConfiguration;
        mPresentation = iPresentation;
    }
    
    
    //
    // Getters & setters
    //
    
    @XmlElement
    public final UUID getKiosk() {
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
