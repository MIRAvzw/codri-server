/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.web;

import be.mira.adastra3.server.bo.Repository;
import be.mira.adastra3.spring.Slf4jLogger;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
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
    
    
    //
    // Construction and destruction
    //
    
    @Required
    public void setRepository(final Repository iRepository) {
        mRepository = iRepository;
    }
    
    
    //
    // REST endpoints
    //
    
    @Profiled(tag="GET api/repository")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Repository getRepository() {        
        return mRepository;
    }
}
