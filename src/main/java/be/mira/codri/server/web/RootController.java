/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.web;

import java.net.UnknownHostException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author tim
 */
@Controller
@RequestMapping("/")
public class RootController {
    @RequestMapping("*")
    public final ModelAndView getIndex() {
        ModelAndView tModel = new ModelAndView("/WEB-INF/jsp/index.jsp");
        
        try {
            tModel.addObject("hostname", java.net.InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException tException) {
            tModel.addObject("hostname", "unknown");
        }
        
        return tModel;
    }    
}
