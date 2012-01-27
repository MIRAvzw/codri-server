/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.bo.repository.presentation;

import be.mira.adastra3.server.bo.repository.RepositoryEntity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tim
 */
@XmlRootElement(name="presentation")
public class Presentation extends RepositoryEntity {   
    
    //
    // Construction and destruction
    //
    
    public Presentation(final Long iRevision, final String iPath) {
        super(iRevision, iPath);
    }
}
