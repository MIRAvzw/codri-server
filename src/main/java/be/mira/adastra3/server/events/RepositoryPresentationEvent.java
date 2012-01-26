/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.events;

import be.mira.adastra3.server.repository.presentation.Presentation;

/**
 *
 * @author tim
 */
public class RepositoryPresentationEvent extends RepositoryEvent {
    //
    // Construction and destruction
    //
    
    public RepositoryPresentationEvent(Object iSource, RepositoryEvent.RepositoryEventType iType, String iId, Presentation iPresentation) {
        super(iSource, iType, iId, iPresentation);
    }
    
    public RepositoryPresentationEvent(Object iSource, RepositoryEvent.RepositoryEventType iType, String iId, Presentation iPresentation, Presentation iOldPresentation) {
        super(iSource, iType, iId, iPresentation, iOldPresentation);
    }
}
