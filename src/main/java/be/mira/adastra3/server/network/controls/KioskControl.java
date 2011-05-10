/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.kiosk.GetInterfaceRevision;
import be.mira.adastra3.server.network.actions.kiosk.LoadInterface;
import be.mira.adastra3.server.network.actions.kiosk.Reboot;
import be.mira.adastra3.server.network.actions.kiosk.SetInterfaceLocation;
import be.mira.adastra3.server.network.actions.kiosk.Shutdown;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.types.ServiceId;

/**
 *
 * @author tim
 */
public class KioskControl extends Control {
    //
    // Data members
    //
    
    public static ServiceId ServiceId = new ServiceId("mira-be", "Kiosk:1");
    
    
    //
    // Construction and destruction
    //

    public KioskControl(RemoteService iService) throws NetworkException {
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
    
    public void SetInterfaceLocation(String iInterfaceLocation) throws NetworkException {
        SetInterfaceLocation tAction = new SetInterfaceLocation(getService(), iInterfaceLocation);
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }
    
    public void LoadInterface() throws NetworkException {
        LoadInterface tAction = new LoadInterface(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );        
    }
    
    public Integer GetInterfaceRevision() throws NetworkException {
        GetInterfaceRevision tAction = new GetInterfaceRevision(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );        
        return tAction.GetInterfaceRevisionValue();
    }
}
