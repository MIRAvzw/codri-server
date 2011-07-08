/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.application;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;

/**
 *
 * @author tim
 */
public class DownloadMedia extends ActionInvocation {
    public DownloadMedia(Service service, String iMediaIdentifier, String iMediaLocation) throws NetworkException {
        super(service.getAction("DownloadMedia"));
        try {
            setInput("iMediaIdentifierValue", iMediaIdentifier);
            setInput("iMediaLocationValue", iMediaLocation);
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Media.DownloadMedia", ex);
        }
    }
}