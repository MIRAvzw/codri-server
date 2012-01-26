/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.events;

import be.mira.adastra3.server.repository.configuration.Configuration;

/**
 *
 * @author tim
 */
public class RepositoryConfigurationEvent extends RepositoryEvent {
    //
    // Construction and destruction
    //
    
    public RepositoryConfigurationEvent(final Object iSource, final RepositoryEvent.RepositoryEventType iType, final String iId, final Configuration iConfiguration) {
        super(iSource, iType, iId, iConfiguration);
    }
    
    public RepositoryConfigurationEvent(final Object iSource, final RepositoryEvent.RepositoryEventType iType, final String iId, final Configuration iConfiguration, final Configuration iOldConfiguration) {
        super(iSource, iType, iId, iConfiguration, iOldConfiguration);
    }
}
