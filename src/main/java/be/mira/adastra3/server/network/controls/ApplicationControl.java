/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.application.GetConfigurationRevisionAction;
import be.mira.adastra3.server.network.actions.application.GetMediaAction;
import be.mira.adastra3.server.network.actions.application.SetMediaAction;
import be.mira.adastra3.server.network.actions.application.SetConfigurationRevisionAction;
import be.mira.adastra3.server.repository.media.Media;
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
    
    public final long getConfigurationRevision() throws NetworkException {
        GetConfigurationRevisionAction tAction = new GetConfigurationRevisionAction(getService());
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
        
        return tAction.getConfigurationRevision();
    }
    
    public final void setConfigurationRevision(long iConfigurationRevision) throws NetworkException {
        SetConfigurationRevisionAction tAction = new SetConfigurationRevisionAction(getService(), iConfigurationRevision);
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
    }
    
    public final void setMedia(final Media iMedia) throws NetworkException {
        SetMediaAction tAction = new SetMediaAction(getService(), iMedia.getId(), iMedia.getLocation());
        
        // TODO: use iMedia.Revision
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
    }
    
    public final Media getMedia() throws NetworkException {
        GetMediaAction tAction = new GetMediaAction(getService());
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
        
        Media tMedia = new Media(
                tAction.getIdentifier(),
                tAction.getLocation()
        );
        tMedia.setRevision(tAction.getRevision());        
        return tMedia;
    }
}
