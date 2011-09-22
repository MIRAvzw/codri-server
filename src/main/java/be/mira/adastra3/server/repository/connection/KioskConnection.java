/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.connection;

import java.util.UUID;

/**
 *
 * @author tim
 */
public class KioskConnection extends Connection {
    //
    // Member data
    //
    
    private UUID mKiosk;
    private String mConfiguration;
    private String mPresentation;
    
    
    //
    // Construction and destruction
    //
    
    public KioskConnection(final UUID iKiosk, final String iConfiguration, final String iPresentation) {
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
