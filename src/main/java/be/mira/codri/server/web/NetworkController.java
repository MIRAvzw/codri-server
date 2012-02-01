/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.web;

import be.mira.codri.server.bo.Network;
import be.mira.codri.server.exceptions.NetworkException;
import be.mira.codri.server.bo.network.Kiosk;
import be.mira.codri.server.spring.Slf4jLogger;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
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
    
    // TODO: can we make this final, despite the property injection?
    private Network mNetwork;
    
    
    //
    // Construction and destruction
    //
    
    @Required
    @Autowired
    public void setNetwork(final Network iNetwork) {
        mNetwork = iNetwork;
    }
    
    
    //
    // REST endpoints
    //
    
    @Profiled(tag="api/network.GET")
    @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public Network getNetwork() {        
        return mNetwork;
    }
    
    @Profiled(tag="api/network/kiosks/$id.GET")
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Kiosk getKiosk(final @PathVariable("id") UUID iId, final HttpServletResponse iResponse) throws IOException {
        Kiosk tKiosk = mNetwork.getKiosk(iId);
        if (tKiosk == null) {
            iResponse.sendError(HttpStatus.NOT_FOUND.value());
        }
        return tKiosk;
    }
    
    @Profiled(tag="api/network/kiosks/$id.PUT")
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.PUT)
    public void updateKiosk(final @PathVariable("id") UUID iId, final HttpServletRequest iRequest, final HttpServletResponse iResponse) throws IOException {
        Kiosk tKiosk = mNetwork.getKiosk(iId);
        if (tKiosk == null) {
            iResponse.sendError(HttpStatus.NOT_FOUND.value());            
        } else {
            tKiosk.updateHeartbeat();
        }
    }
    
    @Profiled(tag="api/network/kiosks/$id.POST")
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.POST)
    public void addKiosk(final @RequestBody Kiosk iKiosk, final @PathVariable("id") UUID iId, final HttpServletRequest iRequest, final HttpServletResponse iResponse) throws IOException {
        try {
            iKiosk.setAddress(iRequest.getRemoteAddr());
            mNetwork.addKiosk(iId, iKiosk);
        } catch (NetworkException tException) {
            iResponse.sendError(HttpStatus.CONFLICT.value(), tException.getLocalizedMessage());
        }
    }
}
