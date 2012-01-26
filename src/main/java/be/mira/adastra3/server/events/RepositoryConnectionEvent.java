/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.events;

import be.mira.adastra3.server.repository.connection.Connection;

/**
 *
 * @author tim
 */
public class RepositoryConnectionEvent extends RepositoryEvent {
    //
    // Construction and destruction
    //
    
    public RepositoryConnectionEvent(Object iSource, RepositoryEvent.RepositoryEventType iType, String iId, Connection iConnection) {
        super(iSource, iType, iId, iConnection);
    }
    
    public RepositoryConnectionEvent(Object iSource, RepositoryEvent.RepositoryEventType iType, String iId, Connection iConnection, Connection iOldConnection) {
        super(iSource, iType, iId, iConnection, iOldConnection);
    }
}
