/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.presentation;

import be.mira.adastra3.server.repository.RepositoryEntity;
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
    
    // FIXME: dummy constructor for JAXB (shouldn't be neccesary as JAXB never
    // has to unmarshal this class)
    public Presentation() {
        throw new UnsupportedOperationException();
    }
    
    public Presentation(final Long iRevision, final String iPath, final String iServer) {
        super(iRevision, iPath, iServer);
    }
}
