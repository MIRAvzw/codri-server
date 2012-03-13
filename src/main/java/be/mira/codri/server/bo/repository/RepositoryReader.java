/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.repository;

import be.mira.codri.server.bo.Repository;
import be.mira.codri.server.exceptions.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tim
 */
public abstract class RepositoryReader {
    //
    // Member data
    //
    
    protected final Repository mRepository;
    
    
    //
    // Construction and destruction
    //
    
    @Autowired
    public RepositoryReader(final Repository iRepository) {
        mRepository = iRepository;        
    }
    
    
    //
    // Interface
    //
    
    public abstract void checkout() throws RepositoryException;
    
    public abstract void update() throws RepositoryException;
    
}
