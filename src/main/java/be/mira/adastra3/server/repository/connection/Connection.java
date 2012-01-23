/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.connection;

import be.mira.adastra3.server.repository.RepositoryEntity;
import java.util.UUID;

/**
 *
 * @author tim
 */
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
    
    public Connection(final String iId, final long iRevision, final String iPath, final String iServer, final UUID iKiosk, final String iConfiguration, final String iPresentation) {
        super(iId, iRevision, iPath, iServer);
        mKiosk = iKiosk;
        mConfiguration = iConfiguration;
        mPresentation = iPresentation;
    }
    
    
    //
    // Getters & setters
    //
    
    public final UUID getKiosk() {
        return mKiosk;
    }
    
    public final String getConfiguration() {
        return mConfiguration;
    }
    
    public final String getPresentation() {
        return mPresentation;
    }
    
}
