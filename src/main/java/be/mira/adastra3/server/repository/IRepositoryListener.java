/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.repository.configurations.KioskConfiguration;

/**
 *
 * @author tim
 */
public interface IRepositoryListener {
    public void doConfigurationAdded(KioskConfiguration iConfiguration);
}
