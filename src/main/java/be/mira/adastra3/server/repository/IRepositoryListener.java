/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.presentation.Presentation;

/**
 *
 * @author tim
 */
public interface IRepositoryListener {
    void doRepositoryError(String iMessage, RepositoryException iException);
    void doRepositoryWarning(String iMessage);
    
    void doConfigurationAdded(Configuration iConfiguration);
    void doConfigurationUpdated(Configuration iOldConfiguration, Configuration iConfiguration);
    void doConfigurationRemoved(Configuration iConfiguration);
    
    void doPresentationAdded(Presentation iPresentation);
    void doPresentationUpdated(Presentation iOldPresentation, Presentation iPresentation);
    void doPresentationRemoved(Presentation iPresentation);
    
    void doConnectionAdded(Connection iConnection);
    void doConnectionUpdated(Connection iOldConnection, Connection iConnection);
    void doConnectionRemoved(Connection iConnection);
}
