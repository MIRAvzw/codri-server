/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.application.ShowInterface;
import be.mira.adastra3.server.network.actions.application.ShowMedia;
import be.mira.adastra3.server.network.actions.application.LoadInterface;
import be.mira.adastra3.server.network.actions.application.LoadMedia;
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
    
    public void LoadMedia(String iMediaLocation) throws NetworkException {
        LoadMedia tAction = new LoadMedia(getService(), iMediaLocation);
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }

    public void ShowMedia() throws NetworkException {
        ShowMedia tAction = new ShowMedia(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }

    public void LoadInterface(String iInterfaceLocation) throws NetworkException {
        LoadInterface tAction = new LoadInterface(getService(), iInterfaceLocation);
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }
    
    public void ShowInterface() throws NetworkException {
        ShowInterface tAction = new ShowInterface(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }
}
