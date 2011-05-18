/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.device.GetVolume;
import be.mira.adastra3.server.network.actions.device.Reboot;
import be.mira.adastra3.server.network.actions.device.SetVolume;
import be.mira.adastra3.server.network.actions.device.Shutdown;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.types.ServiceId;

/**
 *
 * @author tim
 */
public class DeviceControl extends Control {
    //
    // Data members
    //
    
    public static ServiceId ServiceId = new ServiceId("mira-be", "Device:1");
    
    
    //
    // Construction and destruction
    //

    public DeviceControl(RemoteService iService) throws NetworkException {
        super(iService);
        if (! iService.getServiceId().equals(ServiceId))
            throw new NetworkException("KioskControl instantiated for a non-KioskService");
    }
    
    
    //
    // Service actions
    //
    
    public void Shutdown() throws NetworkException {
        Shutdown tAction = new Shutdown(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );        
    }    
    
    public void Reboot() throws NetworkException {
        Reboot tAction = new Reboot(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }
    
    public void SetVolume(Integer iVolume) throws NetworkException {        
        SetVolume tAction = new SetVolume(getService(), iVolume);
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }
    
    public Integer GetVolume() throws NetworkException {
        GetVolume tAction = new GetVolume(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
        
        return tAction.GetVolumeValue();
    }    
}
