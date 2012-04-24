/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.web;

import be.mira.codri.server.bo.Network;
import be.mira.codri.server.exceptions.NetworkException;
import be.mira.codri.server.bo.network.entities.Kiosk;
import be.mira.codri.server.spring.Slf4jLogger;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    private final Network mNetwork;
    
    
    //
    // Construction and destruction
    //
    
    @Autowired
    public NetworkController(final Network iNetwork) {
        mNetwork = iNetwork;        
    }
    
    
    //
    // REST endpoints
    //
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final Network getNetwork() {        
        return mNetwork;
    }
    
    @RequestMapping(value = "/kiosks/{id}", method = RequestMethod.GET)
    @ResponseBody
    public final Kiosk getKiosk(@PathVariable("id") final String iId, final HttpServletResponse iResponse) throws IOException {
        Kiosk tKiosk = mNetwork.getKiosk(iId);
        if (tKiosk == null) {
            iResponse.sendError(HttpStatus.GONE.value());
        }
        return tKiosk;
    }
    
    @RequestMapping(value = "/kiosks/{id}/heartbeat", method = RequestMethod.PUT)
    public final void refreshKiosk(@PathVariable("id") final String iId, final HttpServletRequest iRequest, final HttpServletResponse iResponse) throws IOException {
        try {
            mNetwork.refreshKiosk(iId);
        } catch (NetworkException tException) {
            iResponse.sendError(HttpStatus.GONE.value());
        }
    }
    
    @RequestMapping(value = "/kiosks/{id}", method = RequestMethod.POST)
    public final void addKiosk(@RequestBody final Kiosk iKiosk, @PathVariable("id") final String iId, final HttpServletRequest iRequest, final HttpServletResponse iResponse) throws IOException {        
        try {
            mNetwork.addKiosk(iId, iKiosk);
            iResponse.setStatus(HttpStatus.CREATED.value());
            iResponse.setHeader("Location", String.format("/kiosks/%s", iId));
        } catch (NetworkException tException) {
            iResponse.sendError(HttpStatus.CONFLICT.value(), tException.getLocalizedMessage());
        }
    }
    
    @RequestMapping(value = "/kiosks/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public final void removeKiosk(@PathVariable("id") final String iId, final HttpServletResponse iResponse) throws IOException {
        try {
            mNetwork.removeKiosk(iId);
        } catch (NetworkException tException) {
            iResponse.sendError(HttpStatus.GONE.value());
        }
    }
}
