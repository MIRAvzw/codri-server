/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.media;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;

/**
 *
 * @author tim
 */
public class SetMediaLocation extends ActionInvocation {
    public SetMediaLocation(Service service, String iMediaLocation) throws NetworkException {
        super(service.getAction("SetMediaLocation"));
        try {
            setInput("iMediaLocationValue", iMediaLocation);
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Media.SetMediaLocation", ex);
        }
    }
}