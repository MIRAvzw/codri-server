/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.application.GetInterfaceRevision;
import be.mira.adastra3.server.network.actions.application.GetMediaRevision;
import be.mira.adastra3.server.network.actions.application.SetInterface;
import be.mira.adastra3.server.network.actions.application.SetMedia;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.types.ServiceId;

/**
 *
 * @author tim
 */
public class ApplicationControl extends Control {
    //
    // Data members
    //
    
    public static ServiceId ServiceId = new ServiceId("mira-be", "Application:1");
    
    
    //
    // Construction and destruction
    //

    public ApplicationControl(RemoteService iService) throws NetworkException {
        super(iService);
        if (! iService.getServiceId().equals(ServiceId))
            throw new NetworkException("MediaControl instantiated for a non-MediaService");
    }
    
    
    //
    // Service actions
    //
    
    public void SetMedia(String iMediaLocation) throws NetworkException {
        SetMedia tAction = new SetMedia(getService(), iMediaLocation);
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }

    public Integer GetMediaRevision() throws NetworkException {
        GetMediaRevision tAction = new GetMediaRevision(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
        
        return tAction.GetMediaRevisionValue();
    }

    public void SetInterface(String iInterfaceLocation) throws NetworkException {
        SetInterface tAction = new SetInterface(getService(), iInterfaceLocation);
        
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
