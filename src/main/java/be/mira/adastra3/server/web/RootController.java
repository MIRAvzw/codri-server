/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.web;

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
    public ModelAndView getIndex() {
        ModelAndView tModel = new ModelAndView("/WEB-INF/jsp/index.jsp");
        
        try {
            tModel.addObject("hostname", java.net.InetAddress.getLocalHost().getHostName());
        }
        catch (UnknownHostException tException) {
            tModel.addObject("hostname", "unknown");
        }
        
        return tModel;
    }    
}
