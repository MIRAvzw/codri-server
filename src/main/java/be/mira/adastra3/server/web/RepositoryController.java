/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.web;

import be.mira.adastra3.server.business.Repository;
import be.mira.adastra3.spring.Slf4jLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author tim
 */
@Controller
@RequestMapping("/repository")
public class RepositoryController {
    //
    // Member data
    //
    
    @Slf4jLogger
    private Logger mLogger;
    
    private Repository mRepository;
    
    
    //
    // Construction and destruction
    //
    
    public RepositoryController(final Repository iRepository) {
        mRepository = iRepository;
    }
    
    
    //
    // REST endpoints
    //
    
    @RequestMapping(method = RequestMethod.GET)
    public final @ResponseBody
    Repository getRepository() {        
        return mRepository;
    }
}