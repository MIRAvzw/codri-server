/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.controls;

import org.teleal.cling.model.meta.RemoteService;

/**
 *
 * @author tim
 */
public class Control {
    //
    // Member data
    //
    
    RemoteService mService;
    
    
    //
    // Construction and destruction
    //
    
    public Control(RemoteService iService) {
        mService = iService;
    }
    
    
    //
    // Getters and setters
    //
    
    protected RemoteService getService() {
        return mService;
    }
}
