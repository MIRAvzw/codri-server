/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    
    // FIXME: dummy constructor for JAXB (shouldn't be neccesary as JAXB never
    // has to unmarshal this class)
    public Connection() {
        throw new UnsupportedOperationException();
    }
    
    public Connection(final String iId, final Long iRevision, final String iPath, final String iServer, final UUID iKiosk, final String iConfiguration, final String iPresentation) {
        super(iId, iRevision, iPath, iServer);
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
