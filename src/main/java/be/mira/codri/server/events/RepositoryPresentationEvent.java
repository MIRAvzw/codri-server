/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.events;

import be.mira.codri.server.bo.repository.presentation.Presentation;

/**
 *
 * @author tim
 */
public class RepositoryPresentationEvent extends RepositoryEvent {
    //
    // Construction and destruction
    //
    
    public RepositoryPresentationEvent(final Object iSource, final RepositoryEvent.RepositoryEventType iType, final String iId, final Presentation iPresentation) {
        super(iSource, iType, iId, iPresentation);
    }
    
    public RepositoryPresentationEvent(final Object iSource, final RepositoryEvent.RepositoryEventType iType, final String iId, final Presentation iPresentation, final Presentation iOldPresentation) {
        super(iSource, iType, iId, iPresentation, iOldPresentation);
    }
}
