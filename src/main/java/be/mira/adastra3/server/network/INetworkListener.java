/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.ApplicationControl;
import be.mira.adastra3.server.network.controls.DeviceControl;
import java.util.UUID;

/**
 *
 * @author tim
 */
public interface INetworkListener {
    public void doError(String iMessage, NetworkException iException);
    public void doWarning(String iMessage);
    public void doDeviceControlAdded(UUID iUuid, DeviceControl iMediaControl);
    public void doDeviceControlRemoved(UUID iUuid);
    public void doApplicationControlAdded(UUID iUuid, ApplicationControl iApplicationControl);
    public void doApplicationControlRemoved(UUID iUuid);
}
