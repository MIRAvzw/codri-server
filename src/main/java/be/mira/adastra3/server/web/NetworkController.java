/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.web;

import be.mira.adastra3.server.business.Network;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.Kiosk;
import be.mira.adastra3.spring.Slf4jLogger;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
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
    
    @Slf4jLogger
    private Logger mLogger;
    
    private Network mNetwork;
    
    
    //
    // Construction and destruction
    //
    
    public NetworkController(final Network iNetwork) {
        mNetwork = iNetwork;
    }
    
    
    //
    // REST endpoints
    //
    
    @RequestMapping(method=RequestMethod.GET)
    public final @ResponseBody
    Network getNetwork() {        
        return mNetwork;
    }
    
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.POST)
    public final @ResponseBody
    void addKiosk(final @RequestBody Kiosk iKiosk, final @PathVariable("id") UUID iId, final HttpServletRequest iRequest, final HttpServletResponse iResponse) throws IOException {
        try {
            iKiosk.setAddress(iRequest.getRemoteAddr());
            mNetwork.addKiosk(iId, iKiosk);
        } catch (NetworkException tException) {
            iResponse.sendError(HttpStatus.CONFLICT.value(), tException.getLocalizedMessage());
        }
    }
    
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.PUT)
    public final @ResponseBody
    void updateKiosk(final @PathVariable("id") UUID iId, final HttpServletRequest iRequest, final HttpServletResponse iResponse) throws IOException {
        Kiosk tKiosk = mNetwork.getKiosk(iId);
        if (tKiosk == null) {
            iResponse.sendError(HttpStatus.NOT_FOUND.value());            
        } else {
            tKiosk.updateHeartbeat();
        }
    }
}