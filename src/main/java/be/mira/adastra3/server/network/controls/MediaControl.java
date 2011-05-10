/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.media.GetMediaRevision;
import be.mira.adastra3.server.network.actions.media.GetVolume;
import be.mira.adastra3.server.network.actions.media.LoadMedia;
import be.mira.adastra3.server.network.actions.media.SetMediaLocation;
import be.mira.adastra3.server.network.actions.media.SetVolume;
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
    
    public static ServiceId ServiceId = new ServiceId("mira-be", "Media:1");
    
    
    //
    // Construction and destruction
    //

    public MediaControl(RemoteService iService) throws NetworkException {
        super(iService);
        if (! iService.getServiceId().equals(ServiceId))
            throw new NetworkException("MediaControl instantiated for a non-MediaService");
    }
    
    
    //
    // Service actions
    //
    
    public void SetMediaLocation(String iMediaLocation) throws NetworkException {
        SetMediaLocation tAction = new SetMediaLocation(getService(), iMediaLocation);
        
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
