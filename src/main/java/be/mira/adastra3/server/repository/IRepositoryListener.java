/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;

/**
 *
 * @author tim
 */
public interface IRepositoryListener {
    void doRepositoryError(String iMessage, RepositoryException iException);
    void doRepositoryWarning(String iMessage);
    void doKioskConfigurationAdded(KioskConfiguration iKioskConfiguration);
    void doKioskConfigurationUpdated(KioskConfiguration iOldKioskConfiguration, KioskConfiguration iKioskConfiguration);
}
