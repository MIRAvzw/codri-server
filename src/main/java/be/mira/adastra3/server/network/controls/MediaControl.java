/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.device.GetRevisionAction;
import be.mira.adastra3.server.network.actions.media.GetLocationAction;
import be.mira.adastra3.server.network.actions.media.SetLocationAction;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.types.ServiceId;

/**
 *
 * @author tim
 */
public class MediaControl extends Control {
    //
    // Data members
    //
    
    public final static ServiceId cIdentifier = new ServiceId("mira-be", "Media:1");
    
    
    //
    // Construction and destruction
    //

    public MediaControl(final RemoteService iService) throws NetworkException {
        super(iService);
        if (! iService.getServiceId().equals(cIdentifier)) {
            throw new NetworkException("MediaControl instantiated for a non-MediaService");
        }
    }
    
    
    //
    // Service actions
    //
    
    public final long getRevision() throws NetworkException {
        GetRevisionAction tAction = new GetRevisionAction(getService());
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
        
        return tAction.getConfigurationRevision();
    }
    
    public final void setLocation(final String iLocation) throws NetworkException {
        SetLocationAction tAction = new SetLocationAction(getService(), iLocation);
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
    }
    
    public final String getLocation() throws NetworkException {
        GetLocationAction tAction = new GetLocationAction(getService());
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
        
        return tAction.getLocation();
    }
}
