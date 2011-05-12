/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.devices.Device;

/**
 *
 * @author tim
 */
public interface INetworkListener {
    public void doNetworkError(String iMessage, NetworkException iException);
    public void doNetworkWarning(String iMessage);
    public void doDeviceAdded(Device iDevice);
    public void doDeviceRemoved(Device iDevice);
}
