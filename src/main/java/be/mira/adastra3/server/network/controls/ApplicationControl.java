/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.application.LoadInterface;
import be.mira.adastra3.server.network.actions.application.LoadMedia;
import be.mira.adastra3.server.network.actions.application.DownloadInterface;
import be.mira.adastra3.server.network.actions.application.DownloadMedia;
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
    
    public void DownloadMedia(String iMediaLocation) throws NetworkException {
        DownloadMedia tAction = new DownloadMedia(getService(), iMediaLocation);
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }

    public void LoadMedia() throws NetworkException {
        LoadMedia tAction = new LoadMedia(getService());
        
        Network.getControlPoint().execute(
                new ActionCallback.Default(
                    tAction,
                    Network.getControlPoint()
                )
        );
    }

    public void DownloadInterface(String iInterfaceLocation) throws NetworkException {
        DownloadInterface tAction = new DownloadInterface(getService(), iInterfaceLocation);
        
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
}
