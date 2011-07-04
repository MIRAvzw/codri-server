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
public class SetMedia extends ActionInvocation {
    public SetMedia(Service service, String iMedia) throws NetworkException {
        super(service.getAction("SetMedia"));
        try {
            setInput("iMediaValue", iMedia);
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Media.SetMedia", ex);
        }
    }
}