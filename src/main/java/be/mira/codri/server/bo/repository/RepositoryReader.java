/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.codri.server.bo.repository;

import be.mira.codri.server.bo.Repository;
import be.mira.codri.server.exceptions.RepositoryException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author tim
 */
public abstract class RepositoryReader {
    //
    // Member data
    //
    
    protected Repository mRepository;
    
    
    //
    // Construction and destruction
    //
    
    @Required
    @Autowired
    public void setRepository(Repository iRepository) {
        mRepository = iRepository;
    }
    
    //
    // Interface
    //
    
    public abstract void checkout() throws RepositoryException;
    
    public abstract void update() throws RepositoryException;
    
}
