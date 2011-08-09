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
public class LoadMediaAction extends ActionInvocation {
    public LoadMediaAction(final Service iService, final String iMediaIdentifier, final String iMediaLocation) throws NetworkException {
        super(iService.getAction("LoadMedia"));
        try {
            setInput("iMediaIdentifierValue", iMediaIdentifier);
            setInput("iMediaLocationValue", iMediaLocation);
        } catch (InvalidValueException tException) {
            throw new NetworkException("could not invoke Application.LoadMedia", tException);
        }
    }
}
