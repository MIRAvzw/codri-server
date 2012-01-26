/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.web;

import be.mira.adastra3.server.business.Network;
import be.mira.adastra3.spring.Logger;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author tim
 */
@Controller
@RequestMapping("/network")
public class NetworkController {
    //
    // Member data
    //
    
    @Logger
    private Log mLogger;
    
    private Network mNetwork;
    
    
    //
    // Construction and destruction
    //
    
    public NetworkController(Network iNetwork) {
        mNetwork = iNetwork;
    }
    
    
    //
    // REST endpoints
    //
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Network getNetwork() {        
        return mNetwork;
    }
}
