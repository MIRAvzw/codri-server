/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
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
    
    public final static ServiceId cIdentifier = new ServiceId("mira-be", "Application:1");
    
    
    //
    // Construction and destruction
    //

    public ApplicationControl(final RemoteService iService) throws NetworkException {
        super(iService);
        if (! iService.getServiceId().equals(cIdentifier)) {
            throw new NetworkException("MediaControl instantiated for a non-MediaService");
        }
    }
    
    
    //
    // Service actions
    //
    
    public final void loadMedia(final String iMediaIdentifier, final String iMediaLocation) throws NetworkException {
        LoadMedia tAction = new LoadMedia(getService(), iMediaIdentifier, iMediaLocation);
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
    }

    public final void loadInterface(final String iInterfaceIdentifier, final String iInterfaceRole, final String iInterfaceLocation) throws NetworkException {
        LoadInterface tAction = new LoadInterface(getService(), iInterfaceIdentifier, iInterfaceRole, iInterfaceLocation);
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
    }
}
