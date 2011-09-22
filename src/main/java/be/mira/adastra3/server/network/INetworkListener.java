/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;

/**
 *
 * @author tim
 */
public interface INetworkListener {
    void doNetworkError(String iMessage, NetworkException iException);
    void doNetworkWarning(String iMessage);
    void doEntityAdded(NetworkEntity iDevice);
    void doEntityRemoved(NetworkEntity iDevice);
}
