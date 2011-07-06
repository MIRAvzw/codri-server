/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.application;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.Datatype;
import org.teleal.cling.model.types.InvalidValueException;

/**
 *
 * @author tim
 */
public class LoadMedia extends ActionInvocation {
    public LoadMedia(Service service) throws NetworkException {
        super(service.getAction("LoadMedia"));
        try {
            /* No parameters required */
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Media.LoadMedia", ex);
        }
    }
}