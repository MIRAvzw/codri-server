/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.device;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;

/**
 *
 * @author tim
 */
public class SetRevisionAction extends ActionInvocation {
    public SetRevisionAction(final Service iService, final long iRevision) throws NetworkException {
        super(iService.getAction("SetRevision"));
        
        try {
            setInput("iRevisionValue", new UnsignedIntegerFourBytes(iRevision));
        } catch (InvalidValueException tException) {
            throw new NetworkException("could not invoke Device.SetRevision", tException);
        }
    }
}
