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
    public void doError(String iMessage, RepositoryException iException);
    public void doWarning(String iMessage);
    public void doKioskConfigurationAdded(KioskConfiguration iKioskConfiguration);
    public void doKioskConfigurationUpdated(KioskConfiguration iOldKioskConfiguration, KioskConfiguration iConfiguration);
}
