/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.device.GetVolumeAction;
import be.mira.adastra3.server.network.actions.device.RebootAction;
import be.mira.adastra3.server.network.actions.device.SetVolumeAction;
import be.mira.adastra3.server.network.actions.device.ShutdownAction;
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
    
    public final static ServiceId cIdentifier = new ServiceId("mira-be", "Device:1");
    
    
    //
    // Construction and destruction
    //

    public DeviceControl(final RemoteService iService) throws NetworkException {
        super(iService);
        if (! iService.getServiceId().equals(cIdentifier)) {
            throw new NetworkException("KioskControl instantiated for a non-KioskService");
        }
    }
    
    
    //
    // Service actions
    //
    
    public final void shutdown() throws NetworkException {
        ShutdownAction tAction = new ShutdownAction(getService());
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();     
    }    
    
    public final void reboot() throws NetworkException {
        RebootAction tAction = new RebootAction(getService());
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
    }
    
    public final void setVolume(final Integer iVolume) throws NetworkException {        
        SetVolumeAction tAction = new SetVolumeAction(getService(), iVolume);
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
    }
    
    public final Integer getVolume() throws NetworkException {
        GetVolumeAction tAction = new GetVolumeAction(getService());
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
        
        return tAction.getVolume();
    }    
}
