/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.presentation;

import be.mira.adastra3.server.beans.Repository;
import be.mira.adastra3.server.repository.RepositoryEntity;

/**
 *
 * @author tim
 */
public class Presentation extends RepositoryEntity {   
    
    //
    // Construction and destruction
    //
    
    public Presentation(final String iId, final long iRevision, final String iPath, final String iServer) {
        super(iId, iRevision, iPath, iServer);
    }
}
