/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.application.GetConfigurationRevisionAction;
import be.mira.adastra3.server.network.actions.application.GetMediaAction;
import be.mira.adastra3.server.network.actions.application.GetMediaRevisionAction;
import be.mira.adastra3.server.network.actions.application.LoadMediaAction;
import be.mira.adastra3.server.network.actions.application.SetConfigurationRevisionAction;
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
    // Auxiliary classes
    //
    
    public final static class Media {
        // Data members
        private String mIdentifier;
        private String mLocation;
        
        // Construction
        public Media(final String iIdentifier, final String iLocation) {
            mIdentifier = iIdentifier;
            mLocation = iLocation;
        }
        
        // Getters and setters
        public final String getIdentifier() {
            return mIdentifier;
        }
        public final String getLocation() {
            return mLocation;
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
    
    public final long getMediaRevision() throws NetworkException {
        GetMediaRevisionAction tAction = new GetMediaRevisionAction(getService());
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
        
        return tAction.getMediaRevision();
    }
    
    public final void loadMedia(final Media iMedia) throws NetworkException {
        LoadMediaAction tAction = new LoadMediaAction(getService(), iMedia.getIdentifier(), iMedia.getLocation());
        
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
        
        return new Media(
                tAction.getIdentifier(),
                tAction.getLocation()
        );
    }
}
