/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.web;

import be.mira.codri.server.bo.Network;
import be.mira.codri.server.exceptions.NetworkException;
import be.mira.codri.server.bo.network.entities.Kiosk;
import be.mira.codri.server.spring.Slf4jLogger;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.perf4j.aop.Profiled;
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
            iResponse.sendError(HttpStatus.GONE.value());
        }
        return tKiosk;
    }
    
    @Profiled(tag="api/network/kiosks/$id/heartbeat.PUT")
    @RequestMapping(value="/kiosks/{id}/heartbeat", method=RequestMethod.PUT)
    public void refreshKiosk(final @PathVariable("id") UUID iId, final HttpServletRequest iRequest, final HttpServletResponse iResponse) throws IOException {
        try {
            mNetwork.refreshKiosk(iId);
        } catch (NetworkException tException) {
            iResponse.sendError(HttpStatus.GONE.value());
        }
    }
    
    @Profiled(tag="api/network/kiosks/$id.POST")
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.POST)
    public void addKiosk(final @RequestBody Kiosk iKiosk, final @PathVariable("id") UUID iId, final HttpServletRequest iRequest, final HttpServletResponse iResponse) throws IOException {
        try {
            iKiosk.setAddress(iRequest.getRemoteAddr());
            mNetwork.addKiosk(iId, iKiosk);
            iResponse.setStatus(HttpStatus.CREATED.value());
            iResponse.setHeader("Location", String.format("/rest/customers/%s", iId));
        } catch (NetworkException tException) {
            iResponse.sendError(HttpStatus.CONFLICT.value(), tException.getLocalizedMessage());
        }
    }
    
    @Profiled(tag="api/network/kiosks/$id.DELETE")
    @RequestMapping(value="/kiosks/{id}", method=RequestMethod.DELETE)
    @ResponseBody
    public void removeKiosk(final @PathVariable("id") UUID iId, final HttpServletResponse iResponse) throws IOException {
        try {
            mNetwork.removeKiosk(iId);
        } catch (NetworkException tException) {
            iResponse.sendError(HttpStatus.GONE.value());
        }
    }
}
