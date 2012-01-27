/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
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
    
    public RepositoryConnectionEvent(final Object iSource, final RepositoryEvent.RepositoryEventType iType, final String iId, final Connection iConnection) {
        super(iSource, iType, iId, iConnection);
    }
    
    public RepositoryConnectionEvent(final Object iSource, final RepositoryEvent.RepositoryEventType iType, final String iId, final Connection iConnection, final Connection iOldConnection) {
        super(iSource, iType, iId, iConnection, iOldConnection);
    }
}