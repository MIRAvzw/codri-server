/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    
    public RepositoryConfigurationEvent(Object iSource, RepositoryEvent.RepositoryEventType iType, String iId, Configuration iConfiguration) {
        super(iSource, iType, iId, iConfiguration);
    }
    
    public RepositoryConfigurationEvent(Object iSource, RepositoryEvent.RepositoryEventType iType, String iId, Configuration iConfiguration, Configuration iOldConfiguration) {
        super(iSource, iType, iId, iConfiguration, iOldConfiguration);
    }
}
