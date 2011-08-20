/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.entities.Entity;

/**
 *
 * @author tim
 */
public interface INetworkListener {
    void doNetworkError(String iMessage, NetworkException iException);
    void doNetworkWarning(String iMessage);
    void doDeviceAdded(Entity iDevice);
    void doDeviceRemoved(Entity iDevice);
}
