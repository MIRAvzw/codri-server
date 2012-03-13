/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.web;

import be.mira.codri.server.bo.Repository;
import be.mira.codri.server.bo.repository.RepositoryReader;
import be.mira.codri.server.exceptions.RepositoryException;
import be.mira.codri.server.spring.Slf4jLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    private final Repository mRepository;
    private final RepositoryReader mRepositoryReader;
    
    
    //
    // Construction and destruction
    //
    
    @Autowired
    public RepositoryController(final Repository iRepository, final RepositoryReader iRepositoryReader) {
        mRepository = iRepository;
        mRepositoryReader = iRepositoryReader;
    }
    
    
    //
    // REST endpoints
    //
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final Repository getRepository() {        
        return mRepository;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public final void updateRepository() throws RepositoryException {        
        mRepositoryReader.update();
    }
}
