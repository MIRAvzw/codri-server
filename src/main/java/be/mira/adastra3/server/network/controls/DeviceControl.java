/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.actions.device.GetVolumeAction;
import be.mira.adastra3.server.network.actions.device.EchoAction;
import be.mira.adastra3.server.network.actions.device.GetRevisionAction;
import be.mira.adastra3.server.network.actions.device.RebootAction;
import be.mira.adastra3.server.network.actions.device.SetRevisionAction;
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
            throw new NetworkException("DeviceControl instantiated for a non-DeviceControl");
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
    
    public final void setRevision(long iConfigurationRevision) throws NetworkException {
        SetRevisionAction tAction = new SetRevisionAction(getService(), iConfigurationRevision);
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
    }
    
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
    
    public final String echo(String iEcho) throws NetworkException {
        EchoAction tAction = new EchoAction(getService(), iEcho);
        
        new ActionCallback.Default(
                tAction,
                Network.getControlPoint()
        ).run();
        
        return tAction.getEcho();
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
    
    
    //
    // Meta-actions
    //
    
    public final long ping() throws NetworkException { 
        long tStart = System.currentTimeMillis();
        String tEcho = echo("ping");
        long tEnd = System.currentTimeMillis();
        
        if (! tEcho.equals("ping")) {
            throw new NetworkException("echo service returned invalid response");
        }
        
        return tEnd - tStart;
    }
}
