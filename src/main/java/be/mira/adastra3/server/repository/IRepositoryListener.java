/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.Configuration;

/**
 *
 * @author tim
 */
public interface IRepositoryListener {
    void doRepositoryError(String iMessage, RepositoryException iException);
    void doRepositoryWarning(String iMessage);
    void doConfigurationAdded(Configuration iKioskConfiguration);
    void doConfigurationUpdated(Configuration iOldKioskConfiguration, Configuration iKioskConfiguration);
    void doConfigurationRemoved(Configuration iKioskConfiguration);
}
