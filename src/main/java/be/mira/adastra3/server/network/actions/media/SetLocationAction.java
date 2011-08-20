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
public class SetLocationAction extends ActionInvocation {
    public SetLocationAction(final Service iService, final String iLocation) throws NetworkException {
        super(iService.getAction("SetLocation"));
        try {
            setInput("iLocationValue", iLocation);
        } catch (InvalidValueException tException) {
            throw new NetworkException("could not invoke Media.SetLocation", tException);
        }
    }
}
