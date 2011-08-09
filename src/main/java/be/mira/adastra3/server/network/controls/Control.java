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
    
    private RemoteService mService;
    
    
    //
    // Construction and destruction
    //
    
    public Control(final RemoteService iService) {
        mService = iService;
    }
    
    
    //
    // Getters and setters
    //
    
    final protected RemoteService getService() {
        return mService;
    }
}
