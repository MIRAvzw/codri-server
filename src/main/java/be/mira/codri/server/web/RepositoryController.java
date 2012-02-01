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
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
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
    
    // TODO: can we make this final, despite the property injection?
    private Repository mRepository;
    private RepositoryReader mRepositoryReader;
    
    
    //
    // Construction and destruction
    //
    
    @Required
    @Autowired
    public void setRepository(final Repository iRepository) {
        mRepository = iRepository;
    }
    
    @Required
    @Autowired
    public void setRepositoryReader(final RepositoryReader iRepositoryReader) {
        mRepositoryReader = iRepositoryReader;
    }
    
    
    //
    // REST endpoints
    //
    
    @Profiled(tag="api/repository.GET")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Repository getRepository() {        
        return mRepository;
    }
    
    @Profiled(tag="api/repository.PUT")
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public void updateRepository() throws RepositoryException {        
        mRepositoryReader.update();
    }
}
