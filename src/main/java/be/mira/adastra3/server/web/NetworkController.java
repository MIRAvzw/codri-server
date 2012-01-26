/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.web;

import be.mira.adastra3.server.business.Network;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Kiosk;
import be.mira.adastra3.spring.Logger;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody
    Network getNetwork() {        
        return mNetwork;
    }
    
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.POST)
    public @ResponseBody
    void addKiosk(@RequestBody Kiosk iKiosk, @PathVariable("id") UUID iId, HttpServletRequest iRequest, HttpServletResponse iResponse) throws IOException {
        try {
            iKiosk.setAddress(iRequest.getRemoteAddr());
            mNetwork.addKiosk(iId, iKiosk);
        } catch (NetworkException tException) {
            iResponse.sendError(409, tException.getLocalizedMessage());
        }
    }
    
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody
    void updateKiosk(@PathVariable("id") UUID iId, HttpServletRequest iRequest, HttpServletResponse iResponse) throws IOException {
        Kiosk tKiosk = mNetwork.getKiosk(iId);
        if (tKiosk == null) {
            iResponse.sendError(404);            
        } else {
            tKiosk.updateHeartbeat();
        }
    }
}
