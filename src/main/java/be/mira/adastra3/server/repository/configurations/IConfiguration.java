/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import be.mira.adastra3.server.exceptions.RepositoryException;

/**
 *
 * @author tim
 */
public interface IConfiguration {
    public void check() throws RepositoryException;
}
